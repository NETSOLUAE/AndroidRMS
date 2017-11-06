package rmsllcoman.com.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import rmsllcoman.com.Model.AccountSummary;
import rmsllcoman.com.Model.AccountSummaryLines;
import rmsllcoman.com.Model.ContactDetailsAr;
import rmsllcoman.com.Model.ContactLocationAr;
import rmsllcoman.com.Model.EbPolicyDetailChild;
import rmsllcoman.com.Model.EbPolicyDetailGroup;
import rmsllcoman.com.Model.FAQAnswers;
import rmsllcoman.com.Model.FAQQuestions;
import rmsllcoman.com.Model.InstallmentHistory;
import rmsllcoman.com.Model.MasterDataLines;
import rmsllcoman.com.Model.MasterDataSalary;
import rmsllcoman.com.Model.PolicyChild;
import rmsllcoman.com.Model.PolicyGroup;
import rmsllcoman.com.Model.PolicyLineChild;
import rmsllcoman.com.Model.PolicyLineGroup;
import rmsllcoman.com.Activity.RemainderActivity;
import rmsllcoman.com.Activity.FeedBackForm;
import rmsllcoman.com.Activity.HomeActivity;
import rmsllcoman.com.Activity.HomeActivityLines;
import rmsllcoman.com.Activity.HomeActivitySalaryDeduction;
import rmsllcoman.com.Activity.LoginActivity;
import rmsllcoman.com.Model.ClaimDetails;
import rmsllcoman.com.Model.ContactDetails;
import rmsllcoman.com.Model.ContactLocation;
import rmsllcoman.com.Model.MasterData;
import rmsllcoman.com.Model.MemberDetailsChild;
import rmsllcoman.com.Model.MemberDetailsStaff;
import rmsllcoman.com.Model.PdfLinks;
import rmsllcoman.com.Model.PreApproval;
import rmsllcoman.com.Activity.PostCommentActivity;
import rmsllcoman.com.Activity.RegistrationActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by macmini on 6/12/17.
 */

public class WebserviceManager {
    private Context context;
    private static String POST_PARAMS;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public WebserviceManager(Context context) {
        this.context = context;
        Constants.deviceID = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        pref = context.getSharedPreferences("RMSLocalStorage", MODE_PRIVATE);
        editor = pref.edit();
    }

    //General API for EB Portal, Personal Lines, Salary Deduction Scheme
    public String loginAuthentication (String action_id, String phoneNumber, String pin, String uniqueID, String staffID, String clientID, String type){
        String result = "";
        String actionParams = "";
        String pinCode = "";
        URL url = null;
        HttpURLConnection connection = null;

        if (type.equalsIgnoreCase("eb")) {
            actionParams = "mobile_no";
            if (!action_id.contains("reset")) {
                action_id = "login_new";
            }
            pinCode = "password";
        } else if(type.equalsIgnoreCase("salary") || type.equalsIgnoreCase("lines")) {
            actionParams = "national_id";
            pinCode = "pin_code";
        }

        if(staffID.equalsIgnoreCase("") && clientID.equalsIgnoreCase("") && (action_id.equalsIgnoreCase("login") || action_id.equalsIgnoreCase("login_new"))) {
            POST_PARAMS = "action_id=" + action_id + "&" + actionParams + "=" + phoneNumber + "&" + pinCode + "=" + pin + "&device_id=" + uniqueID;
        } else if (action_id.equalsIgnoreCase("login") || action_id.equalsIgnoreCase("login_new")){
            POST_PARAMS = "action_id=" + action_id + "&" + actionParams + "=" + phoneNumber + "&" + pinCode + "=" + pin + "&device_id=" + uniqueID + "&staff_id=" + staffID + "&client_no=" + clientID;
        } else {
            POST_PARAMS = "action_id=" + action_id + "&" + actionParams + "=" + phoneNumber + "&new_pin_code=" + pin + "&device_id=" + uniqueID + "&staff_id=" + staffID + "&client_no=" + clientID;
        }
        try {
            if (type.equalsIgnoreCase("eb")) {
                url = new URL(Constants.BASE_URL);
            } else if(type.equalsIgnoreCase("salary")) {
                url = new URL(Constants.BASE_URL_SALARY);
            } else if(type.equalsIgnoreCase("lines")) {
                url = new URL(Constants.BASE_URL_LINES);
            }
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            os.write(POST_PARAMS.getBytes());
            os.flush();
            os.close();

            int status = ((HttpURLConnection) connection).getResponseCode();
            Log.i("","Status : "+status);

            if (status == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                result = response.toString();
                if (!result.equalsIgnoreCase("")){
                    result = parseloginDetails(result, phoneNumber, type);
                } else {
                    result = "NoData";
                }
            } else {
                result = "URLNotFound";
                System.out.println("POST request not worked");
            }
        } catch (IOException e) {
            result = "URLNotFound";
            e.printStackTrace();
        }
        return result;
    }

    private String parseloginDetails(String response, String phoneNumber, String type) {
        DatabaseManager dbManager = new DatabaseManager(context);
        String statusString = "";
        String deviceID =  Constants.deviceID;
        try {
            JSONObject loginResp = new JSONObject(response);

            String status = loginResp
                    .getString("status");
            String message1 = loginResp
                    .getString("message");
            LoginActivity.message = message1;
            if (type.equalsIgnoreCase("eb")) {
                HomeActivity.memberMessage = message1;
            } else if (type.equalsIgnoreCase("salary")) {
                HomeActivitySalaryDeduction.memberMessage = message1;
            } else if (type.equalsIgnoreCase("lines")) {
                HomeActivityLines.memberMessage = message1;
            }
            if (status.equalsIgnoreCase("success")) {
                statusString = status;
            } else {
                statusString = status;
            }
            if (loginResp.has("data")) {
                JSONObject master = loginResp.getJSONObject("data");
                if (type.equalsIgnoreCase("eb")) {
                    dbManager.clearMasterDetails();
                    MasterData masterData = new MasterData();
                    masterData.setMobileNo(master
                            .getString("mobile_no"));
                    masterData.setStaffID(master
                            .getString("staff_id"));
                    masterData.setClientID(master
                            .getString("client_no"));
                    masterData.setChangePin(master
                            .getString("change_pin"));
                    editor.putString("phone_number", master
                            .getString("mobile_no"));
                    editor.apply();
                    dbManager.insertMasterData(masterData);
                } else if(type.equalsIgnoreCase("salary")) {
                    dbManager.clearMasterDetailsSalary();
                    MasterDataSalary masterDataSalary = new MasterDataSalary();
                    masterDataSalary.setNationalId(phoneNumber);
                    masterDataSalary.setStaffID(master
                            .getString("staff_id"));
                    masterDataSalary.setClientID(master
                            .getString("client_no"));
                    masterDataSalary.setChangePin(master
                            .getString("change_pin"));
                    dbManager.insertMasterDataSalary(masterDataSalary);
                } else if(type.equalsIgnoreCase("lines")) {
                    dbManager.clearMasterDetailsLines();
                    MasterDataLines masterDataLines = new MasterDataLines();
                    masterDataLines.setNationalId(phoneNumber);
                    masterDataLines.setStaffID(master
                            .getString("staff_id"));
                    masterDataLines.setClientID(master
                            .getString("client_no"));
                    masterDataLines.setChangePin(master
                            .getString("change_pin"));
                    dbManager.insertMasterDataLines(masterDataLines);
                }
            }

        } catch (JSONException e) {
            statusString = "NoData";
            e.printStackTrace();
            Log.v("Exception is " + Log.getStackTraceString(e), deviceID, e);
        }
        return statusString;
    }

    public String postComments (String clientId, String memberId, String staffId, String name, String subject, String mobile_no, String comments, String type){
        String result = "";
        URL url = null;
        HttpURLConnection connection = null;
        try {
            List<Pair> nameValuePairs = new ArrayList<Pair>(8);
            Pair<String, String> member_id = null;
            Pair<String, String> action_id = new Pair<>("action_id", "post_comments");
            Pair<String, String> client_idPair = new Pair<>("client_no", clientId);
            if (type.equalsIgnoreCase("MemberDetails")) {
                member_id = new Pair<>("member_id", memberId);
            } else if (type.equalsIgnoreCase("salaryDeductionScheme") || type.equalsIgnoreCase("personalLines") ) {
                member_id = new Pair<>("national_id", memberId);
            }
            Pair<String, String> staff_id = new Pair<>("staff_id", staffId);
            Pair<String, String> namePair = new Pair<>("name", name);
            Pair<String, String> subjectPair = new Pair<>("subject", subject);
            Pair<String, String> mobile_noPair = new Pair<>("mobile_no", mobile_no);
            Pair<String, String> commentsPair = new Pair<>("comments", comments);
            nameValuePairs.add(action_id);
            nameValuePairs.add(client_idPair);
            nameValuePairs.add(member_id);
            nameValuePairs.add(staff_id);
            nameValuePairs.add(namePair);
            nameValuePairs.add(subjectPair);
            nameValuePairs.add(mobile_noPair);
            nameValuePairs.add(commentsPair);

            if (type.equalsIgnoreCase("MemberDetails")) {
                url = new URL(Constants.BASE_URL);
            } else if (type.equalsIgnoreCase("salaryDeductionScheme")) {
                url = new URL(Constants.BASE_URL_SALARY);
            } else if (type.equalsIgnoreCase("personalLines")) {
                url = new URL(Constants.BASE_URL_LINES);
            } else {
                url = new URL(Constants.BASE_URL);
            }
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(nameValuePairs));
            writer.flush();
            writer.close();
            os.close();

            connection.connect();

            int status = ((HttpURLConnection) connection).getResponseCode();
            Log.i("","Status : "+status);

            if (status == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                result = response.toString();
                if (!result.equalsIgnoreCase("")){
                    JSONObject postCommentsResp = new JSONObject(result);

                    String statusPostComments = postCommentsResp
                            .getString("status");

                    String messagePostComments = postCommentsResp
                            .getString("message");
                    PostCommentActivity.message = messagePostComments;
                    result = statusPostComments;
                } else {
                    result = "NoData";
                }
            } else {
                result = "URLNotFound";
                System.out.println("POST request not worked");
            }
        } catch (IOException e) {
            result = "URLNotFound";
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String sms (String actionId, String mobileNo, String staffId, String clientNo, String nationalID1, String type){
        String result = "";
        String urlString = "";
        URL url = null;
        HttpURLConnection connection = null;
        if (type.equalsIgnoreCase("eb")) {
            urlString = Constants.BASE_URL + "?action_id=" + actionId + "&mobile_no=" + mobileNo + "&staff_id=" + staffId + "&client_no=" + clientNo + "&national_id=" + nationalID1;
        } else if (type.equalsIgnoreCase("salary")) {
            urlString = Constants.BASE_URL_SALARY + "?action_id=" + actionId + "&national_id=" + mobileNo + "&staff_id=" + staffId + "&client_no=" + clientNo;
        } else if (type.equalsIgnoreCase("lines")) {
            urlString = Constants.BASE_URL_LINES + "?action_id=" + actionId + "&national_id=" + mobileNo + "&staff_id=" + staffId + "&client_no=" + clientNo;
        }
        try {
            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int status = ((HttpURLConnection) connection).getResponseCode();
            Log.i("","Status : "+status);

            if (status == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                result = response.toString();
                if (!result.equalsIgnoreCase("") || !result.equalsIgnoreCase(" []")){
                    JSONObject smsResp = new JSONObject(result);

                    String smsstatus = smsResp
                            .getString("status");
                    String smsMessage = smsResp
                            .getString("message");
                    LoginActivity.message = smsMessage;
                    if (smsstatus.equalsIgnoreCase("success")){
                        result = "success";
                    } else {
                        result = "fail";
                    }
                } else {
                    result = "NoData";
                }
            } else {
                result = "URLNotFound";
                System.out.println("POST request not worked");
            }
        } catch (IOException e) {
            result = "URLNotFound";
            e.printStackTrace();
        } catch (JSONException e) {
            result = "NoData";
            e.printStackTrace();
        }
        return result;
    }

    //API calls for EB Portal
    public String memberDetailsCall (String actionID, String phoneNumber, String staffId, String clientNo){
        String result = "";
        URL url = null;
        HttpURLConnection connection = null;
        String urlString = Constants.BASE_URL + "?action_id=" + actionID + "&mobile_no=" + phoneNumber + "&staff_id=" + staffId + "&client_no=" + clientNo;
        try {
            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int status = ((HttpURLConnection) connection).getResponseCode();
            Log.i("","Status : "+status);

            if (status == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                result = response.toString();
                if (!result.equalsIgnoreCase("")){
                    result = parseMemberDetails(result);
                } else {
                    result = "NoData";
                }
            } else {
                result = "URLNotFound";
                System.out.println("POST request not worked");
            }
        } catch (IOException e) {
            result = "URLNotFound";
            e.printStackTrace();
        }
        return result;
    }

    private String parseMemberDetails(String response) {
        DatabaseManager dbManager = new DatabaseManager(context);
        String statusString = "";
        String deviceID =  Constants.deviceID;
        try {
            JSONObject authenticationResp = new JSONObject(response);

            String require_login_update = authenticationResp
                    .getString("require_update");

            JSONArray staffDetailsArray = authenticationResp.getJSONArray("data");
            for (int i = 0; i < staffDetailsArray.length(); i++) {
                JSONObject staffDetailsObject = staffDetailsArray
                        .getJSONObject(i);
                if (require_login_update.equalsIgnoreCase("yes")){
                    dbManager.clearStaffDetais();
                    MemberDetailsStaff member_details = new MemberDetailsStaff();
                    member_details.setClientID(staffDetailsObject
                            .getString("client_id"));
                    member_details.setStaffID(staffDetailsObject
                            .getString("staff_id"));
                    member_details.setPolicyRef(staffDetailsObject
                            .getString("policy_ref"));
                    member_details.setMemberID(staffDetailsObject
                            .getString("member_id"));
                    member_details.setMemberName(staffDetailsObject
                            .getString("member_name"));
                    member_details.setRelationship(staffDetailsObject
                            .getString("relationship"));
                    member_details.setGender(staffDetailsObject
                            .getString("dender"));
                    member_details.setNationality(staffDetailsObject
                            .getString("nationality"));
                    member_details.setPhoneNumber(staffDetailsObject.getString("phone"));
                    member_details.setEffectiveDate(staffDetailsObject
                            .getString("effective_date"));
                    member_details.setEmail(staffDetailsObject
                            .getString("email"));
                    dbManager.insertStaffDetails(member_details);

                    if (staffDetailsObject.isNull("dependents")) {
                        Log.d("", "");
                    } else {
                        JSONArray dependentDetailsArray = staffDetailsObject.getJSONArray("dependents");
                        for (int j = 0; j < dependentDetailsArray.length(); j++) {
                            JSONObject dependentDetailsObject = dependentDetailsArray
                                    .getJSONObject(j);
                            MemberDetailsChild dependent_Details = new MemberDetailsChild();
                            dependent_Details.setClientID(dependentDetailsObject
                                    .getString("client_id"));
                            dependent_Details.setStaffID(dependentDetailsObject
                                    .getString("staff_id"));
                            dependent_Details.setPolicyRef(dependentDetailsObject
                                    .getString("policy_ref"));
                            dependent_Details.setMemberID(dependentDetailsObject
                                    .getString("member_id"));
                            dependent_Details.setMemberName(dependentDetailsObject
                                    .getString("member_name"));
                            dependent_Details.setRelationship(dependentDetailsObject
                                    .getString("relationship"));
                            dependent_Details.setGender(dependentDetailsObject
                                    .getString("dender"));
                            dependent_Details.setNationality(dependentDetailsObject
                                    .getString("nationality"));
                            dependent_Details.setPhoneNumber(dependentDetailsObject
                                    .getString("phone"));
                            dependent_Details.setEffectiveDate(dependentDetailsObject
                                    .getString("effective_date"));
                            dependent_Details.setEmail(dependentDetailsObject
                                    .getString("email"));
                            dbManager.insertDependentDetails(dependent_Details);

                        }
                    }

                    if (staffDetailsObject.isNull("pdfs")) {
                        Log.d("", "");
                    } else {
                        JSONObject pdfObj = staffDetailsObject
                                .getJSONObject("pdfs");
                        for (int k = 0; k < pdfObj.length(); k++) {
                            PdfLinks pdfLinks = new PdfLinks();
                            if (k == 0) {
                                pdfLinks.setPdfName("Member Guide");
                                pdfLinks.setPdfLink(pdfObj
                                        .getString("member_guide"));
                            } else if (k == 1) {
                                pdfLinks.setPdfName("Hospital Network");
                                pdfLinks.setPdfLink(pdfObj
                                        .getString("hospital_network"));
                            } else if (k == 2) {
                                pdfLinks.setPdfName("Claim Form");
                                pdfLinks.setPdfLink(pdfObj
                                        .getString("claim_form"));
                            } else if (k == 3) {
                                pdfLinks.setPdfName("Pre Approval Form");
                                pdfLinks.setPdfLink(pdfObj
                                        .getString("pre_approval_form"));
                            }
                            dbManager.insertPdfLinks(pdfLinks);
                        }
                    }
                    statusString = "Updated";
                }else {
                    statusString = "NoUpdate";
                }
            }

        } catch (JSONException e) {
            statusString = "NoData";
            e.printStackTrace();
            Log.v("Exception is " + Log.getStackTraceString(e), deviceID, e);
        }
        return statusString;
    }

    public String policyDetailsCall (String actionID, String phoneNumber, String staffId, String clientNo){
        String result = "";
        URL url = null;
        HttpURLConnection connection = null;
        String urlString = Constants.BASE_URL + "?action_id=" + actionID + "&mobile_no=" + phoneNumber + "&staff_id=" + staffId + "&client_no=" + clientNo;
        try {
            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int status = ((HttpURLConnection) connection).getResponseCode();
            Log.i("","Status : "+status);

            if (status == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                result = response.toString();
                if (!result.equalsIgnoreCase("")){
                    JSONObject policyResp = new JSONObject(result);

                    String statusPolicy = policyResp
                            .getString("status");
                    if (statusPolicy.equalsIgnoreCase("success")) {
                        result = parsePolicyDetails(result);
                    } else {
                        result = statusPolicy;
                    }
                } else {
                    result = "NoData";
                }
            } else {
                result = "URLNotFound";
                System.out.println("POST request not worked");
            }
        } catch (IOException e) {
            result = "URLNotFound";
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String parsePolicyDetails(String response) {
        DatabaseManager dbManager = new DatabaseManager(context);
        String statusString = "";
        String deviceID =  Constants.deviceID;
        try {
            JSONObject authenticationResp = new JSONObject(response);

            String require_login_update = authenticationResp
                    .getString("require_update");

            if (require_login_update.equalsIgnoreCase("yes")){
                dbManager.clearEbPolicyDetails();
                JSONObject staffDetailsArray = authenticationResp.getJSONObject("data");
                EbPolicyDetailGroup ebPolicyDetailGroup = new EbPolicyDetailGroup();
                ebPolicyDetailGroup.setMemberName(staffDetailsArray.getString("member_name"));
                ebPolicyDetailGroup.setmemberID(staffDetailsArray.getString("member_id"));
                ebPolicyDetailGroup.setRelationship("P");
                dbManager.insertEbPolicyDetailsGroup(ebPolicyDetailGroup);

                if (staffDetailsArray.isNull("policies")) {
                    Log.d("", "");
                } else {
                    JSONArray dependentDetailsArray = staffDetailsArray.getJSONArray("policies");
                    for (int j = 0; j < dependentDetailsArray.length(); j++) {
                        JSONObject dependentDetailsObject = dependentDetailsArray
                                .getJSONObject(j);
                        EbPolicyDetailChild ebPolicyDetailChild = new EbPolicyDetailChild();
                        ebPolicyDetailChild.setMemberID(staffDetailsArray
                                .getString("member_id"));
                        ebPolicyDetailChild.setStaffId(dependentDetailsObject
                                .getString("staff_id"));
                        ebPolicyDetailChild.setCompanyName(dependentDetailsObject
                                .getString("company_name"));
                        ebPolicyDetailChild.setPolicyRef(dependentDetailsObject
                                .getString("policy_no"));
                        ebPolicyDetailChild.setstartDate(dependentDetailsObject
                                .getString("start_date"));
                        ebPolicyDetailChild.setEndDate(dependentDetailsObject
                                .getString("end_date"));
                        dbManager.insertEbPolicyDetailsChild(ebPolicyDetailChild);
                    }
                }

                if (staffDetailsArray.isNull("dependents")) {
                    Log.d("", "");
                } else {
                    JSONArray dependentDetailsArray = staffDetailsArray.getJSONArray("dependents");
                    for (int j = 0; j < dependentDetailsArray.length(); j++) {
                        JSONObject dependentDetailsObject = dependentDetailsArray
                                .getJSONObject(j);
                        EbPolicyDetailGroup ebPolicyDetailGroup1 = new EbPolicyDetailGroup();
                        ebPolicyDetailGroup1.setMemberName(dependentDetailsObject.getString("member_name"));
                        ebPolicyDetailGroup1.setmemberID(dependentDetailsObject.getString("member_id"));
                        ebPolicyDetailGroup1.setRelationship("D");
                        dbManager.insertEbPolicyDetailsGroup(ebPolicyDetailGroup1);
                        if (dependentDetailsObject.isNull("plicies")) {
                            Log.d("", "");
                        } else {
                            JSONArray dependentDetailsArray1 = dependentDetailsObject.getJSONArray("plicies");
                            for (int k = 0; k < dependentDetailsArray1.length(); k++) {
                                JSONObject dependentDetailsObject1 = dependentDetailsArray1
                                        .getJSONObject(k);
                                EbPolicyDetailChild ebPolicyDetailChild = new EbPolicyDetailChild();
                                ebPolicyDetailChild.setMemberID(dependentDetailsObject
                                        .getString("member_id"));
                                ebPolicyDetailChild.setStaffId(dependentDetailsObject1
                                        .getString("staff_id"));
                                ebPolicyDetailChild.setCompanyName(dependentDetailsObject1
                                        .getString("company_name"));
                                ebPolicyDetailChild.setPolicyRef(dependentDetailsObject1
                                        .getString("policy_no"));
                                ebPolicyDetailChild.setstartDate(dependentDetailsObject1
                                        .getString("start_date"));
                                ebPolicyDetailChild.setEndDate(dependentDetailsObject1
                                        .getString("end_date"));
                                dbManager.insertEbPolicyDetailsChild(ebPolicyDetailChild);
                            }
                        }
                    }
                }
                statusString = "Updated";
            } else {
                statusString = "NoUpdate";
            }

        } catch (JSONException e) {
            statusString = "NoData";
            e.printStackTrace();
            Log.v("Exception is " + Log.getStackTraceString(e), deviceID, e);
        }
        return statusString;
    }

    public String claimDetailsCall (String actionId, String staffId, String clientNo){
        String result = "";
        String urlString = "";
        URL url = null;
        HttpURLConnection connection = null;
        urlString = Constants.BASE_URL + "?action_id=" + actionId + "&staff_id=" + staffId + "&client_no=" + clientNo;
        try {
            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int status = ((HttpURLConnection) connection).getResponseCode();
            Log.i("","Status : "+status);

            if (status == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                result = response.toString();
                if (!result.equalsIgnoreCase("") || !result.equalsIgnoreCase(" []")){
                    result = parseClaimDetails(result);
                } else {
                    result = "NoData";
                }
            } else {
                result = "URLNotFound";
                System.out.println("POST request not worked");
            }
        } catch (IOException e) {
            result = "URLNotFound";
            e.printStackTrace();
        }
        return result;
    }

    private String parseClaimDetails(String response) {
        DatabaseManager dbManager = new DatabaseManager(context);
        String statusString = "";
        String deviceID =  Constants.deviceID;
        try {
            JSONObject claimResp = new JSONObject(response);

            String statusPre = claimResp
                    .getString("status");
            String require_claim_update = claimResp
                    .getString("require_update");
            if (statusPre.equalsIgnoreCase("fail")){
                dbManager.clearClaimDetais();
                statusString = "NoDataAvailable";
            } else {
                JSONArray claimDetailsArray = claimResp.getJSONArray("data");
                if (require_claim_update.equalsIgnoreCase("yes")){
                    dbManager.clearClaimDetais();

                    for (int i = 0; i < claimDetailsArray.length(); i++) {
                        JSONObject claimDetailsAObject = claimDetailsArray
                                .getJSONObject(i);
                        ClaimDetails claimStatus = new ClaimDetails();
                        claimStatus.setClaimNo(claimDetailsAObject
                                .getString("claim_no"));
                        claimStatus.setRegisteredDate(claimDetailsAObject
                                .getString("reg_date"));
                        claimStatus.setPatientName(claimDetailsAObject
                                .getString("member_name"));
                        claimStatus.setStaffName(claimDetailsAObject
                                .getString("staff_name"));
                        claimStatus.setTreatmentDate(claimDetailsAObject
                                .getString("treatment_date"));
                        claimStatus.setStatus(claimDetailsAObject
                                .getString("status"));
                        claimStatus.setMemberType(claimDetailsAObject
                                .getString("member_type"));
                        claimStatus.setClaimedAmount(claimDetailsAObject
                                .getString("amount"));
                        claimStatus.setApprovedAmount(claimDetailsAObject
                                .getString("payment"));
                        claimStatus.setExcess(claimDetailsAObject
                                .getString("excess"));
                        claimStatus.setDisallowance(claimDetailsAObject
                                .getString("disallowance"));
                        claimStatus.setSettledAmountRO(claimDetailsAObject
                                .getString("ref_amnt"));
                        claimStatus.setModeOfPayment(claimDetailsAObject
                                .getString("mode_of_payment"));
                        claimStatus.setChequeNo(claimDetailsAObject
                                .getString("cheque_no"));
                        claimStatus.setSettledAmount(claimDetailsAObject
                                .getString("cheque_rec_date"));
                        claimStatus.setRemarks(claimDetailsAObject
                                .getString("remarks"));
                        claimStatus.setPolicyRefNo(claimDetailsAObject
                                .getString("policy_no"));
                        claimStatus.setDiagnosis(claimDetailsAObject
                                .getString("diagnosis"));
                        dbManager.insertClaimDetails(claimStatus);
                    }
                    statusString = "Updated";
                } else {
                    statusString = "NoUpdate";
                }
            }
        } catch (JSONException e) {
            statusString = "NoData";
            e.printStackTrace();
            Log.v("Exception is " + Log.getStackTraceString(e), deviceID, e);
        }
        return statusString;
    }

    public String preApprovalCall (String actionId, String staffId, String clientNo){
        String result = "";
        String urlString = "";
        URL url = null;
        HttpURLConnection connection = null;
        urlString = Constants.BASE_URL + "?action_id=" + actionId + "&staff_id=" + staffId + "&client_no=" + clientNo;
        try {
            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int status = ((HttpURLConnection) connection).getResponseCode();
            Log.i("","Status : "+status);

            if (status == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String inputLine;
                StringBuffer responseClaim = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    responseClaim.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(responseClaim.toString());
                result = responseClaim.toString();
                if (!result.equalsIgnoreCase("")){
                    result = parsePreApprovalDetails(result);
                } else {
                    result = "NoData";
                }
            } else {
                result = "URLNotFound";
                System.out.println("POST request not worked");
            }
        } catch (IOException e) {
            result = "URLNotFound";
            e.printStackTrace();
        }
        return result;
    }

    private String parsePreApprovalDetails(String response) {
        DatabaseManager dbManager = new DatabaseManager(context);
        String statusString = "";
        String deviceID =  Constants.deviceID;
        try {
            JSONObject preApprovalResp = new JSONObject(response);

            String statusPre = preApprovalResp
                    .getString("status");
            String require_preapproval_update = preApprovalResp
                    .getString("require_update");
            if (statusPre.equalsIgnoreCase("fail")){
                dbManager.clearPreApprovalDetails();
                statusString = "NoDataAvailable";
            } else {
                JSONArray preApprovalsArray = preApprovalResp.getJSONArray("data");
                if (require_preapproval_update.equalsIgnoreCase("yes")){
                    dbManager.clearPreApprovalDetails();

                    for (int i = 0; i < preApprovalsArray.length(); i++) {
                        JSONObject preApprovalObject = preApprovalsArray
                                .getJSONObject(i);
                        PreApproval preApproval = new PreApproval();
                        preApproval.setPatientId(preApprovalObject
                                .getString("memberid"));
                        preApproval.setPatientName(preApprovalObject
                                .getString("patient_name"));
                        preApproval.setStaffId(preApprovalObject
                                .getString("staff_id"));
                        preApproval.setStaffName(preApprovalObject
                                .getString("staff_name"));
                        preApproval.setPolicyRefNo(preApprovalObject
                                .getString("pol_ref"));
                        preApproval.setEntryDate(preApprovalObject
                                .getString("entry_dt"));
                        preApproval.setDiagnosis(preApprovalObject
                                .getString("diagnosis"));
                        preApproval.setPlace(preApprovalObject
                                .getString("place_code"));
                        preApproval.setHospitalName(preApprovalObject
                                .getString("hospital_name"));
                        preApproval.setPreApprovalNo(preApprovalObject
                                .getString("pre_approvalno"));
                        preApproval.setPreApprovalDate(preApprovalObject
                                .getString("pre_approvaldt"));
                        preApproval.setRemarks(preApprovalObject
                                .getString("remarks"));
                        preApproval.setStatus(preApprovalObject
                                .getString("status"));
                        dbManager.insertPreApproval(preApproval);
                    }
                    statusString = "Updated";
                } else {
                    statusString = "NoUpdate";
                }
            }

        } catch (JSONException e) {
            statusString = "NoData";
            e.printStackTrace();
            Log.v("Exception is " + Log.getStackTraceString(e), deviceID, e);
        }
        return statusString;
    }

    //API calls for Salary Deduction Scheme Portal
    public String policyDetailsSalaryCall (String actionID, String nationalId){
        String result = "";
        URL url = null;
        HttpURLConnection connection = null;
        String urlString = Constants.BASE_URL_SALARY + "?action_id=" + actionID + "&national_id=" + nationalId;
        try {
            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int status = ((HttpURLConnection) connection).getResponseCode();
            Log.i("","Status : "+status);

            if (status == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                result = response.toString();
                if (!result.equalsIgnoreCase("")){
                    result = parsePolicyDetailsSalary(result);
                } else {
                    result = "NoData";
                }
            } else {
                result = "URLNotFound";
                System.out.println("POST request not worked");
            }
        } catch (IOException e) {
            result = "URLNotFound";
            e.printStackTrace();
        }
        return result;
    }

    private String parsePolicyDetailsSalary(String response) {
        DatabaseManager dbManager = new DatabaseManager(context);
        String statusString = "";
        String deviceID =  Constants.deviceID;
        try {
            JSONObject authenticationResp = new JSONObject(response);

            JSONArray staffDetailsArray = authenticationResp.getJSONArray("data");
            dbManager.clearPolicyDetais();
            for (int i = 0; i < staffDetailsArray.length(); i++) {
                JSONObject staffDetailsObject = staffDetailsArray
                        .getJSONObject(i);
                PolicyGroup policyGroup = new PolicyGroup();
                policyGroup.setNationalID(staffDetailsObject
                        .getString("national_id"));
                policyGroup.setStaffId(staffDetailsObject
                        .getString("staff_id"));
                policyGroup.setClientId(staffDetailsObject
                        .getString("client_no"));
                policyGroup.setName(staffDetailsObject
                        .getString("staff_name"));
                dbManager.insertStaffDetailsSalary(policyGroup);


                PolicyChild policyChild = new PolicyChild();
                policyChild.setNationalID(staffDetailsObject
                        .getString("national_id"));
                policyChild.setStaffId(staffDetailsObject
                        .getString("staff_id"));
                policyChild.setNumber(staffDetailsObject
                        .getString("policy_no"));
                policyChild.setStartDate(staffDetailsObject
                        .getString("start_date"));
                policyChild.setExpiryDate(staffDetailsObject
                        .getString("expiry_date"));
                dbManager.insertStaffDetailsSalaryPolicy(policyChild);
                statusString = "Updated";
            }

        } catch (JSONException e) {
            statusString = "NoData";
            e.printStackTrace();
            Log.v("Exception is " + Log.getStackTraceString(e), deviceID, e);
        }
        return statusString;
    }

    public String accountSummaryCall (String actionID, String nationalId){
        String result = "";
        URL url = null;
        HttpURLConnection connection = null;
        String urlString = Constants.BASE_URL_SALARY + "?action_id=" + actionID + "&national_id=" + nationalId;
        try {
            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int status = ((HttpURLConnection) connection).getResponseCode();
            Log.i("","Status : "+status);

            if (status == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                result = response.toString();
                if (!result.equalsIgnoreCase("")){
                    result = parseAccountSummary(result);
                } else {
                    result = "NoData";
                }
            } else {
                result = "URLNotFound";
                System.out.println("POST request not worked");
            }
        } catch (IOException e) {
            result = "URLNotFound";
            e.printStackTrace();
        }
        return result;
    }

    private String parseAccountSummary(String response) {
        DatabaseManager dbManager = new DatabaseManager(context);
        String statusString = "";
        String deviceID =  Constants.deviceID;
        try {
            JSONObject authenticationResp = new JSONObject(response);

            JSONArray staffDetailsArray = authenticationResp.getJSONArray("data");
            dbManager.clearAccountSummay();
            for (int i = 0; i < staffDetailsArray.length(); i++) {
                JSONObject staffDetailsObject = staffDetailsArray
                        .getJSONObject(i);
                AccountSummary accountSummary = new AccountSummary();
                accountSummary.setNationalID(staffDetailsObject
                        .getString("national_id"));
                accountSummary.setStaffId(staffDetailsObject
                        .getString("staff_id"));
                accountSummary.setVehicleNo(staffDetailsObject
                        .getString("vehicle_no"));
                accountSummary.setStatus(staffDetailsObject
                        .getString("status"));
                accountSummary.setOutstandingAmount(staffDetailsObject
                        .getString("outstanding_amount"));
                accountSummary.setTotalPremium(staffDetailsObject
                        .getString("tot_premium"));
                accountSummary.setNextInstallmentAmount(staffDetailsObject
                        .getString("next_installment_amount"));
                accountSummary.setInsurer(staffDetailsObject
                        .getString("insurrer"));
                accountSummary.setStartDate(staffDetailsObject
                        .getString("policy_start_date"));
                accountSummary.setEndDate(staffDetailsObject
                        .getString("policy_expiry_date"));
                accountSummary.setPolicyNo(staffDetailsObject
                        .getString("policy_no"));
                accountSummary.setPolicyYear(staffDetailsObject
                        .getString("policy_year"));
                dbManager.insertAccountSummary(accountSummary);

                if (staffDetailsObject.isNull("installments")) {
                    Log.d("", "");
                } else {
                    JSONArray installmentsArray = staffDetailsObject.getJSONArray("installments");
                    for (int j = 0; j < installmentsArray.length(); j++) {
                        JSONObject installmentsObject = installmentsArray
                                .getJSONObject(j);
                        String amount = installmentsObject
                                .getString("amount");
                        if (!amount.equalsIgnoreCase("null")) {
                            InstallmentHistory installmentDetails = new InstallmentHistory();
                            installmentDetails.setVehicleNo(staffDetailsObject
                                    .getString("vehicle_no"));
                            installmentDetails.setDate(installmentsObject
                                    .getString("date"));
                            installmentDetails.setAmount(installmentsObject
                                    .getString("amount"));
                            installmentDetails.setStatus(installmentsObject
                                    .getString("status"));
                            dbManager.insertInstallmentHistory(installmentDetails);
                        }

                    }
                }
                statusString = "Updated";
            }

        } catch (JSONException e) {
            statusString = "NoData";
            e.printStackTrace();
            Log.v("Exception is " + Log.getStackTraceString(e), deviceID, e);
        }
        return statusString;
    }

    //API calls for Personal Lines Portal
    public String policyDetailsLinesCall (String actionID, String nationalId){
        String result = "";
        URL url = null;
        HttpURLConnection connection = null;
        String urlString = Constants.BASE_URL_LINES + "?action_id=" + actionID + "&national_id=" + nationalId;
        try {
            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int status = ((HttpURLConnection) connection).getResponseCode();
            Log.i("","Status : "+status);

            if (status == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                result = response.toString();
                if (!result.equalsIgnoreCase("")){
                    result = parsePolicyDetailsLines(result);
                } else {
                    result = "NoData";
                }
            } else {
                result = "URLNotFound";
                System.out.println("POST request not worked");
            }
        } catch (IOException e) {
            result = "URLNotFound";
            e.printStackTrace();
        }
        return result;
    }

    private String parsePolicyDetailsLines(String response) {
        DatabaseManager dbManager = new DatabaseManager(context);
        String statusString = "";
        String deviceID =  Constants.deviceID;
        try {
            JSONObject authenticationResp = new JSONObject(response);

            JSONArray staffDetailsArray = authenticationResp.getJSONArray("data");
            dbManager.clearPolicyDetaisLines();
            for (int i = 0; i < staffDetailsArray.length(); i++) {
                JSONObject staffDetailsObject = staffDetailsArray
                        .getJSONObject(i);
                PolicyLineGroup policyLinesGroup = new PolicyLineGroup();
                policyLinesGroup.setNationalID(staffDetailsObject
                        .getString("national_id"));
                policyLinesGroup.setStaffId(staffDetailsObject
                        .getString("staff_id"));
                policyLinesGroup.setClientId(staffDetailsObject
                        .getString("client_no"));
                policyLinesGroup.setName(staffDetailsObject
                        .getString("staff_name"));
                dbManager.insertStaffDetailsLines(policyLinesGroup);


                PolicyLineChild policyLinesChild = new PolicyLineChild();
                policyLinesChild.setNationalID(staffDetailsObject
                        .getString("national_id"));
                policyLinesChild.setStaffId(staffDetailsObject
                        .getString("staff_id"));
                policyLinesChild.setNumber(staffDetailsObject
                        .getString("policy_no"));
                policyLinesChild.setStartDate(staffDetailsObject
                        .getString("start_date"));
                policyLinesChild.setExpiryDate(staffDetailsObject
                        .getString("expiry_date"));
                dbManager.insertStaffDetailsLinesPolicy(policyLinesChild);
                statusString = "Updated";
            }

        } catch (JSONException e) {
            statusString = "NoData";
            e.printStackTrace();
            Log.v("Exception is " + Log.getStackTraceString(e), deviceID, e);
        }
        return statusString;
    }

    public String accountSummaryCallLines (String actionID, String nationalId){
        String result = "";
        URL url = null;
        HttpURLConnection connection = null;
        String urlString = Constants.BASE_URL_LINES + "?action_id=" + actionID + "&national_id=" + nationalId;
        try {
            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int status = ((HttpURLConnection) connection).getResponseCode();
            Log.i("","Status : "+status);

            if (status == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                result = response.toString();
                if (!result.equalsIgnoreCase("")){
                    result = parseAccountSummaryLines(result);
                } else {
                    result = "NoData";
                }
            } else {
                result = "URLNotFound";
                System.out.println("POST request not worked");
            }
        } catch (IOException e) {
            result = "URLNotFound";
            e.printStackTrace();
        }
        return result;
    }

    private String parseAccountSummaryLines(String response) {
        DatabaseManager dbManager = new DatabaseManager(context);
        String statusString = "";
        String deviceID =  Constants.deviceID;
        try {
            JSONObject authenticationResp = new JSONObject(response);

            JSONArray staffDetailsArray = authenticationResp.getJSONArray("data");
            dbManager.clearAccountSummaryLines();
            for (int i = 0; i < staffDetailsArray.length(); i++) {
                JSONObject staffDetailsObject = staffDetailsArray
                        .getJSONObject(i);
                AccountSummaryLines accountSummaryLines = new AccountSummaryLines();
                accountSummaryLines.setNationalID(staffDetailsObject
                        .getString("national_id"));
                accountSummaryLines.setStaffId(staffDetailsObject
                        .getString("staff_id"));
                accountSummaryLines.setVehicleNo(staffDetailsObject
                        .getString("vehicle_no"));
                accountSummaryLines.setStatus(staffDetailsObject
                        .getString("status"));
                accountSummaryLines.setOutstandingAmount(staffDetailsObject
                        .getString("outstanding_amount"));
                accountSummaryLines.setTotalPremium(staffDetailsObject
                        .getString("tot_premium"));
                accountSummaryLines.setNextInstallmentAmount(staffDetailsObject
                        .getString("next_installment_amount"));
                accountSummaryLines.setInsurer(staffDetailsObject
                        .getString("insurrer"));
                accountSummaryLines.setStartDate(staffDetailsObject
                        .getString("policy_start_date"));
                accountSummaryLines.setEndDate(staffDetailsObject
                        .getString("policy_expiry_date"));
                accountSummaryLines.setPolicyNo(staffDetailsObject
                        .getString("policy_no"));
                accountSummaryLines.setPolicyYear(staffDetailsObject
                        .getString("policy_year"));
                dbManager.insertAccountSummaryLines(accountSummaryLines);
                statusString = "Updated";
            }

        } catch (JSONException e) {
            statusString = "NoData";
            e.printStackTrace();
            Log.v("Exception is " + Log.getStackTraceString(e), deviceID, e);
        }
        return statusString;
    }

    //API calls for Contact Info
    public String companyAddressCall (String actionID){
        String result = "";
        URL url = null;
        HttpURLConnection connection = null;
        String urlString = Constants.BASE_URL + "?action_id=" + actionID;
        try {
            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int status = ((HttpURLConnection) connection).getResponseCode();
            Log.i("","Status : "+status);

            if (status == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                result = response.toString();
                if (!result.equalsIgnoreCase("")){
                    result = parseCompanyAddress(result);
                } else {
                    result = "NoData";
                }
            } else {
                result = "URLNotFound";
                System.out.println("POST request not worked");
            }
        } catch (IOException e) {
            result = "URLNotFound";
            e.printStackTrace();
        }
        return result;
    }

    private String parseCompanyAddress(String response) {
        DatabaseManager dbManager = new DatabaseManager(context);
        dbManager.clearContactInfo();
        dbManager.clearContactInfoAr();
        String statusString = "";
        String deviceID =  Constants.deviceID;
        try {
            JSONObject companyResp = new JSONObject(response);

            JSONArray staffDetailsArray = companyResp.getJSONArray("data");

            for (int i = 0; i < staffDetailsArray.length(); i++) {
                JSONObject staffDetailsObject = staffDetailsArray
                        .getJSONObject(i);
                ContactLocation contactLocation = new ContactLocation();
                contactLocation.setID(String.valueOf(i));
                contactLocation.setLocation(staffDetailsObject
                        .getString("country"));
                dbManager.insertContactLocation(contactLocation);

                if (staffDetailsObject.isNull("addresses")) {
                    Log.d("", "");
                } else {
                    JSONArray addressesDetailsArray = staffDetailsObject.getJSONArray("addresses");
                    for (int j = 0; j < addressesDetailsArray.length(); j++) {
                        JSONObject addressesDetailsObject = addressesDetailsArray
                                .getJSONObject(j);

                        ContactDetails contactDetails = new ContactDetails();
                        contactDetails.setID(String.valueOf(j));
                        contactDetails.setIndex(String.valueOf(i));
                        contactDetails.setBranchName(addressesDetailsObject
                                .getString("branch_name"));
                        contactDetails.setAttention(addressesDetailsObject
                                .getString("attention"));
                        contactDetails.setPO_Box(addressesDetailsObject
                                .getString("p_o_box"));
                        contactDetails.setPostalCode(addressesDetailsObject
                                .getString("postal_code"));
                        contactDetails.setAddress(addressesDetailsObject
                                .getString("address"));
                        contactDetails.setTelephone(addressesDetailsObject
                                .getString("telephone"));
                        contactDetails.setMobile(addressesDetailsObject
                                .getString("mobile"));
                        contactDetails.setFax(addressesDetailsObject
                                .getString("fax"));
                        contactDetails.setEmail(addressesDetailsObject
                                .getString("email"));
                        contactDetails.setHotLine(addressesDetailsObject
                                .getString("hot_line"));
                        contactDetails.setDirect_lines(addressesDetailsObject
                                .getString("direct_line"));
                        dbManager.insertContactDetails(contactDetails);

                        if (!(addressesDetailsObject.getString("country_ar")).equalsIgnoreCase("null") ) {

                            if (j == 0) {
                                ContactLocationAr contactLocationAr = new ContactLocationAr();
                                contactLocationAr.setID_ar(String.valueOf(i));
                                contactLocationAr.setLocation_ar(addressesDetailsObject
                                        .getString("country_ar"));
                                dbManager.insertContactLocationAr(contactLocationAr);
                            }
                            JSONObject addressesDetailsObjectAr = addressesDetailsArray
                                    .getJSONObject(j);
                            ContactDetailsAr contactDetailsAr = new ContactDetailsAr();
                            contactDetailsAr.setID_ar(String.valueOf(j));
                            contactDetailsAr.setIndex_ar(String.valueOf(i));
                            contactDetailsAr.setBranchName_ar(addressesDetailsObjectAr
                                    .getString("branch_name_ar"));
                            contactDetailsAr.setAttention_ar(addressesDetailsObjectAr
                                    .getString("attention_ar"));
                            contactDetailsAr.setPO_Box_ar(addressesDetailsObjectAr
                                    .getString("p_o_box_ar"));
                            contactDetailsAr.setPostalCode_ar(addressesDetailsObjectAr
                                    .getString("postal_code_ar"));
                            contactDetailsAr.setAddress_ar(addressesDetailsObjectAr
                                    .getString("address_ar"));
                            contactDetailsAr.setTelephone_ar(addressesDetailsObjectAr
                                    .getString("telephone_ar"));
                            contactDetailsAr.setMobileAr(addressesDetailsObjectAr
                                    .getString("mobile_ar"));
                            contactDetailsAr.setFax_ar(addressesDetailsObjectAr
                                    .getString("fax_ar"));
                            contactDetailsAr.setEmail_ar(addressesDetailsObjectAr
                                    .getString("email_ar"));
                            contactDetailsAr.setHotLine_ar(addressesDetailsObjectAr
                                    .getString("hot_line_ar"));
                            dbManager.insertContactDetailsAr(contactDetailsAr);
                        }
                    }
                }
                statusString = "Updated";
            }

        } catch (JSONException e) {
            statusString = "NoData";
            e.printStackTrace();
            Log.v("Exception is " + Log.getStackTraceString(e), deviceID, e);
        }
        return statusString;
    }

    //API calls for FAQ
    public String faqCall (String actionID){
        String result = "";
        URL url = null;
        HttpURLConnection connection = null;
        String urlString = Constants.BASE_URL + "?action_id=" + actionID;
        try {
            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int status = ((HttpURLConnection) connection).getResponseCode();
            Log.i("","Status : "+status);

            if (status == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                result = response.toString();
                if (!result.equalsIgnoreCase("")){
                    result = parseFaq(result);
                } else {
                    result = "NoData";
                }
            } else {
                result = "URLNotFound";
                System.out.println("POST request not worked");
            }
        } catch (IOException e) {
            result = "URLNotFound";
            e.printStackTrace();
        }
        return result;
    }

    private String parseFaq(String response) {
        DatabaseManager dbManager = new DatabaseManager(context);
        dbManager.clearFaq();
        String statusString = "";
        String deviceID =  Constants.deviceID;
        try {
            JSONObject companyResp = new JSONObject(response);

            JSONArray staffDetailsArray = companyResp.getJSONArray("data");

            for (int i = 0; i < staffDetailsArray.length(); i++) {
                JSONObject staffDetailsObject = staffDetailsArray
                        .getJSONObject(i);
                FAQQuestions faqQuestions = new FAQQuestions();
                faqQuestions.setID(String.valueOf(i));
                String title = staffDetailsObject
                        .getString("title");
                title = title.replaceAll("'","\'");
                faqQuestions.setTitle(title);
                dbManager.insertFaqQuestions(faqQuestions);


                FAQAnswers faqAnswers = new FAQAnswers();
                faqAnswers.setID(String.valueOf(i));
                faqAnswers.setIndex(String.valueOf(i));
                String desc = staffDetailsObject
                        .getString("description");
                desc = desc.replaceAll("'","\'");
                faqAnswers.setDescription(desc);
                dbManager.insertFaqAnswers(faqAnswers);

//                if (staffDetailsObject.isNull("description")) {
//                    Log.d("", "");
//                } else {
//                    JSONArray addressesDetailsArray = staffDetailsObject.getJSONArray("description");
//                    for (int j = 0; j < addressesDetailsArray.length(); j++) {
//                        JSONObject addressesDetailsObject = addressesDetailsArray
//                                .getJSONObject(j);
//
//                        FAQAnswers faqAnswers = new FAQAnswers();
//                        faqAnswers.setID(String.valueOf(j));
//                        faqAnswers.setIndex(staffDetailsObject
//                                .getString("id"));
//                        faqAnswers.setDescription(addressesDetailsObject
//                                .getString("description"));
//                        dbManager.insertFaqAnswers(faqAnswers);
//                    }
//                }
                statusString = "Updated";
            }

        } catch (JSONException e) {
            statusString = "NoData";
            e.printStackTrace();
            Log.v("Exception is " + Log.getStackTraceString(e), deviceID, e);
        }
        return statusString;
    }

    //API calls for FeedBack Form
    public String feedBack (String name, String mobile_no, String contact_type, String subject, String description){
        String result = "";
        URL url = null;
        HttpURLConnection connection = null;
        try {
            List<Pair> nameValuePairs = new ArrayList<Pair>(8);
            Pair<String, String> action_id = new Pair<>("action_id", "post_feedback");
            Pair<String, String> namePair = new Pair<>("name", name);
            Pair<String, String> mobileno_id = new Pair<>("mobile_no", mobile_no);
            Pair<String, String> contact_types = new Pair<>("contact_types", contact_type);
            Pair<String, String> subjectPair = new Pair<>("subject", subject);
            Pair<String, String> descriptionPair = new Pair<>("description", description);
            nameValuePairs.add(action_id);
            nameValuePairs.add(namePair);
            nameValuePairs.add(mobileno_id);
            nameValuePairs.add(contact_types);
            nameValuePairs.add(subjectPair);
            nameValuePairs.add(descriptionPair);

            url = new URL(Constants.BASE_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(nameValuePairs));
            writer.flush();
            writer.close();
            os.close();

            connection.connect();

            int status = ((HttpURLConnection) connection).getResponseCode();
            Log.i("","Status : "+status);

            if (status == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                result = response.toString();
                if (!result.equalsIgnoreCase("")){
                    JSONObject postCommentsResp = new JSONObject(result);

                    String statusPostComments = postCommentsResp
                            .getString("status");
                    FeedBackForm.message = postCommentsResp
                            .getString("message");
                    result = statusPostComments;
                } else {
                    result = "NoData";
                }
            } else {
                result = "URLNotFound";
                System.out.println("POST request not worked");
            }
        } catch (IOException e) {
            result = "URLNotFound";
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    //API calls for Remainder Form
    public String remainder (String name, String insurance_type, String date_of_birth, String nationalID, String mobile_no, String email, String insuranceStartDate, String insuranceEndDate, String message){
        String result = "";
        URL url = null;
        HttpURLConnection connection = null;
        try {
            List<Pair> nameValuePairs = new ArrayList<Pair>(8);
            Pair<String, String> action_id = new Pair<>("action_id", "send_reminder");
            Pair<String, String> namePair = new Pair<>("full_name", name);
            Pair<String, String> nationalIDPair = new Pair<>("national_id", nationalID);
            Pair<String, String> insuranceStartDatePair = new Pair<>("start_date", insuranceStartDate);
            Pair<String, String> insuranceEndDatePair = new Pair<>("end_date", insuranceEndDate);
            Pair<String, String> mobileno_id = new Pair<>("mobile_no", mobile_no);
            Pair<String, String> dob = new Pair<>("date_of_birth", date_of_birth);
            Pair<String, String> contact_types = new Pair<>("insurance_type", insurance_type);
            Pair<String, String> subjectPair = new Pair<>("email", email);
            Pair<String, String> descriptionPair = new Pair<>("message", message);
            nameValuePairs.add(action_id);
            nameValuePairs.add(namePair);
            nameValuePairs.add(nationalIDPair);
            nameValuePairs.add(insuranceStartDatePair);
            nameValuePairs.add(insuranceEndDatePair);
            nameValuePairs.add(dob);
            nameValuePairs.add(mobileno_id);
            nameValuePairs.add(contact_types);
            nameValuePairs.add(subjectPair);
            nameValuePairs.add(descriptionPair);

            url = new URL(Constants.BASE_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(nameValuePairs));
            writer.flush();
            writer.close();
            os.close();

            connection.connect();

            int status = ((HttpURLConnection) connection).getResponseCode();
            Log.i("","Status : "+status);

            if (status == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                result = response.toString();
                if (!result.equalsIgnoreCase("")){
                    JSONObject postCommentsResp = new JSONObject(result);

                    String statusPostComments = postCommentsResp
                            .getString("status");
                    RemainderActivity.message = postCommentsResp
                            .getString("message");
                    result = statusPostComments;
                } else {
                    result = "NoData";
                }
            } else {
                result = "URLNotFound";
                System.out.println("POST request not worked");
            }
        } catch (IOException e) {
            result = "URLNotFound";
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    //API calls for Registration
    public String registration (String action_id, String name, String dob, String mobile, String companyName, String email, String nationalID, String userName, String password){
        String result = "";
        URL url = null;
        HttpURLConnection connection = null;
        String urlString = Constants.BASE_URL + "?action_id=" + action_id;
        try {
            POST_PARAMS = "action_id=" + action_id + "&full_name=" + name + "&date_of_birth=" + dob + "&mobile_no=" + mobile + "&company_name=" + companyName + "" +
                    "&email=" + email + "&national_id=" + nationalID + "&username=" + userName + "&password=" + password;

            url = new URL(Constants.BASE_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            os.write(POST_PARAMS.getBytes());
            os.flush();
            os.close();

            int status = ((HttpURLConnection) connection).getResponseCode();
            Log.i("","Status : "+status);

            if (status == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                result = response.toString();
                if (!result.equalsIgnoreCase("")){
                    JSONObject postCommentsResp = new JSONObject(result);

                    String statusPostComments = postCommentsResp
                            .getString("status");
                    String feedbackComments = postCommentsResp
                            .getString("message");
                    RegistrationActivity.message = feedbackComments;
                    result = statusPostComments;
                } else {
                    result = "NoData";
                }
            } else {
                result = "URLNotFound";
                System.out.println("POST request not worked");
            }
        } catch (IOException e) {
            result = "URLNotFound";
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getQuery(List<Pair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Pair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode((String) pair.first, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode((String) pair.second, "UTF-8"));
        }

        return result.toString();
    }
}
