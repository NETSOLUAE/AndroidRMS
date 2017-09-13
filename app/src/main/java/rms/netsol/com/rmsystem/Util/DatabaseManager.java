package rms.netsol.com.rmsystem.Util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import rms.netsol.com.rmsystem.Model.AccountSummary;
import rms.netsol.com.rmsystem.Model.AccountSummaryLines;
import rms.netsol.com.rmsystem.Model.ClaimDetails;
import rms.netsol.com.rmsystem.Model.ContactDetails;
import rms.netsol.com.rmsystem.Model.ContactDetailsAr;
import rms.netsol.com.rmsystem.Model.ContactLocation;
import rms.netsol.com.rmsystem.Model.ContactLocationAr;
import rms.netsol.com.rmsystem.Model.InstallmentHistory;
import rms.netsol.com.rmsystem.Model.Installments;
import rms.netsol.com.rmsystem.Model.MasterData;
import rms.netsol.com.rmsystem.Model.MasterDataLines;
import rms.netsol.com.rmsystem.Model.MasterDataSalary;
import rms.netsol.com.rmsystem.Model.MemberDetailsChild;
import rms.netsol.com.rmsystem.Model.MemberDetailsStaff;
import rms.netsol.com.rmsystem.Model.PdfLinks;
import rms.netsol.com.rmsystem.Model.PolicyChild;
import rms.netsol.com.rmsystem.Model.PolicyGroup;
import rms.netsol.com.rmsystem.Model.PolicyLineChild;
import rms.netsol.com.rmsystem.Model.PolicyLineGroup;
import rms.netsol.com.rmsystem.Model.PreApproval;

/**
 * Created by macmini on 6/13/17.
 */

public class DatabaseManager {
    private static String TAG = Constants.LOG_RMS + DatabaseManager.class.getSimpleName();
    private SQLiteDatabase db;
    private static final String DATABASE_NAME = "RMS.db";
    private static final int DATABASE_VERSION = 2;
    private Context context;
    private static String dbPassword = "";
    RMSDataHelper openHelper;

    public DatabaseManager(Context context) {
        this.context = context;
        dbPassword = Encryption.toAscii(context);
    }

    private void openConnection() {
        try {
            if (this.db == null || !this.db.isOpen()) {
                openHelper = new RMSDataHelper(this.context);
                this.db = openHelper.getWritableDatabase();
            }
        } catch (Exception ex) {
            Log.e(TAG, "Exception is " + Log.getStackTraceString(ex));
        }
    }

    private void closeConnection() {
        try {
            if (this.db != null && this.db.isOpen()) {
                db.close();
            }
        } catch (Exception ex) {
            Log.e(TAG, "Exception is " + Log.getStackTraceString(ex));
        }
    }

    /**
     * This method creates all the tables.
     */

    public void createDb() {
        openConnection();

        // To Create EB Tables
        db.execSQL("CREATE Table MASTER_DATA(MOBILE_NUMBER TEXT, CLIENT_ID TEXT, STAFF_ID TEXT, CHANGE_PIN TEXT)");
        db.execSQL("CREATE Table STAFF_DETAILS(CLIENT_ID TEXT, STAFF_ID TEXT, POLICY_REF TEXT, MEMBER_ID TEXT, MEMBER_NAME TEXT, RELATIONSHIP TEXT, GENDER TEXT, NATIONALITY TEXT, PHONE_NUMBER TEXT, EFFECTIVE_DATE TEXT, EMAIL TEXT)");
        db.execSQL("CREATE Table DEPENDENT_DETAILS(CLIENT_ID TEXT, STAFF_ID TEXT, POLICY_REF TEXT, MEMBER_ID TEXT, MEMBER_NAME TEXT, RELATIONSHIP TEXT, GENDER TEXT, NATIONALITY TEXT, PHONE_NUMBER TEXT, EFFECTIVE_DATE TEXT, EMAIL TEXT)");
        db.execSQL("CREATE Table PDF_DETAILS(PDF_NAME TEXT, PDF_LINK TEXT)");
        db.execSQL("CREATE Table CLAIM_DETAILS(CLAIM_NO TEXT, REGISTERED_DATE TEXT, MEMBER_NAME TEXT, STAFF_NAME TEXT, TREATMENT_DATE TEXT, AMOUNT TEXT, STATUS TEXT, MEMBER_TYPE TEXT, CLAIMED_AMOUNT TEXT, APPROVED_AMOUNT TEXT, EXCESS TEXT, DISALLOWANCE TEXT, SETTLED_AMOUNTRO TEXT, MODE_OF_PAYMENT TEXT, CHEQUE_NO TEXT, SETTLED_DATE TEXT, REMARKS TEXT, POLICY_NO TEXT, DIAGNOSIS TEXT)");
        db.execSQL("CREATE Table PRE_APPROVAL(MEMBER_ID TEXT, PATIENT_NAME TEXT, STAFF_ID TEXT, STAFF_NAME TEXT, POLICY_REF TEXT, ENTRY_DATE TEXT, DIAGNOSIS TEXT, PALCE_CODE TEXT, HOSPITAL_NAME TEXT, PRE_APPROVAL_NO TEXT, PRE_APPROVAL_DATE TEXT, REMARKS TEXT, STATUS TEXT)");

        // To Create MonthlySalaryDeduction Tables
        db.execSQL("CREATE Table MASTER_DATA_SALARY(NATIONAL_ID TEXT, CLIENT_ID TEXT, STAFF_ID TEXT, CHANGE_PIN TEXT)");
        db.execSQL("CREATE Table STAFF_DETAILS_SALARY(NATIONAL_ID TEXT, STAFF_ID TEXT, CLIENT_ID TEXT, STAFF_NAME TEXT)");
        db.execSQL("CREATE Table STAFF_DETAILS_POLICY(NATIONAL_ID TEXT, STAFF_ID TEXT, POLICY_NO TEXT, START_DATE TEXT, END_DATE TEXT)");
        db.execSQL("CREATE Table ACCOUNT_SUMMARY(NATIONAL_ID TEXT, STAFF_ID TEXT, VEHICLE_NO TEXT, STATUS TEXT, OUTSTANDING_AMOUNT TEXT, TOTAL_PREMIUM TEXT, NEXT_INSTALLMENT_AMOUNT TEXT, INSURER TEXT," +
                "START_DATE TEXT, END_DATE TEXT, POLICY_NO TEXT, POLICY_YEAR TEXT)");
        db.execSQL("CREATE Table POLICY_INSTALLMENT(VEHICLE_NO TEXT, DATE TEXT, AMOUNT TEXT, STATUS TEXT)");

        // To Create PersonalLines Tables
        db.execSQL("CREATE Table MASTER_DATA_LINES(NATIONAL_ID TEXT, CLIENT_ID TEXT, STAFF_ID TEXT, CHANGE_PIN TEXT)");
        db.execSQL("CREATE Table STAFF_DETAILS_LINES(NATIONAL_ID TEXT, STAFF_ID TEXT, CLIENT_ID TEXT, STAFF_NAME TEXT)");
        db.execSQL("CREATE Table STAFF_DETAILS_POLICY_LINES(NATIONAL_ID TEXT, STAFF_ID TEXT, POLICY_NO TEXT, START_DATE TEXT, END_DATE TEXT)");
        db.execSQL("CREATE Table ACCOUNT_SUMMARY_LINES(NATIONAL_ID TEXT, STAFF_ID TEXT, VEHICLE_NO TEXT, STATUS TEXT, OUTSTANDING_AMOUNT TEXT, TOTAL_PREMIUM TEXT, NEXT_INSTALLMENT_AMOUNT TEXT, INSURER TEXT," +
                "START_DATE TEXT, END_DATE TEXT, POLICY_NO TEXT, POLICY_YEAR TEXT)");

        // To Create ContactDetails Tables
        db.execSQL("CREATE Table CONTACT_COUNTRY(ID TEXT PRIMARY KEY, COUNTRY TEXT)");
        db.execSQL("CREATE Table CONTACT_DETAILS(ID TEXT, INDEXS TEXT, BRANCH_NAME TEXT, ATTENTION TEXT, P_O_BOX TEXT, POSTAL_CODE TEXT, ADDRESS TEXT, TELEPHONE TEXT, MOBILE TEXT, " +
                "FAX TEXT, EMAIL TEXT, HOT_LINE TEXT, DIRECT_LINE TEXT)");
        db.execSQL("CREATE Table CONTACT_COUNTRY_AR(ID_AR TEXT PRIMARY KEY, COUNTRY_AR TEXT)");
        db.execSQL("CREATE Table CONTACT_DETAILS_AR(ID_AR TEXT, INDEXS_AR TEXT, BRANCH_NAME_AR TEXT, ATTENTION_AR TEXT, P_O_BOX_AR TEXT, POSTAL_CODE_AR TEXT, ADDRESS_AR TEXT, TELEPHONE_AR TEXT, " +
                "MOBILE_AR TEXT, FAX_AR TEXT, EMAIL_AR TEXT, HOT_LINE_AR TEXT)");

        closeConnection();
    }

    /**
     * Start of EB Portal Table Transaction
     */

    public void clearMasterDetails() {
        openConnection();
        // To clear transaction Tables
        db.execSQL("DELETE FROM MASTER_DATA");
        closeConnection();
    }

    public void clearStaffDetais() {
        openConnection();
        // To clear transaction Tables
        db.execSQL("DELETE FROM STAFF_DETAILS");
        db.execSQL("DELETE FROM DEPENDENT_DETAILS");
        db.execSQL("DELETE FROM PDF_DETAILS");
        closeConnection();
    }

    public void clearClaimDetais() {
        openConnection();
        // To clear transaction Tables
        db.execSQL("DELETE FROM CLAIM_DETAILS");
        closeConnection();
    }

    public void clearPreApprovalDetails() {
        openConnection();
        // To clear transaction Tables
        db.execSQL("DELETE FROM PRE_APPROVAL");
        closeConnection();
    }

    public String getClientId() {

        String clientID = "";
        openConnection();
        Cursor c = db
                .rawQuery(
                        "SELECT CLIENT_ID FROM MASTER_DATA;",
                        null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            clientID = String.valueOf(c.getString(c
                    .getColumnIndex("CLIENT_ID")));

        }
        c.close();
        closeConnection();
        return clientID;

    }

    public String getMemberID() {

        String clientID = "";
        openConnection();
        Cursor c = db
                .rawQuery(
                        "SELECT MEMBER_ID FROM STAFF_DETAILS;",
                        null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            clientID = String.valueOf(c.getString(c
                    .getColumnIndex("MEMBER_ID")));

        }
        c.close();
        closeConnection();
        return clientID;

    }

    public String getStaffId() {

        String clientID = "";
        openConnection();
        Cursor c = db
                .rawQuery(
                        "SELECT STAFF_ID FROM MASTER_DATA;",
                        null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            clientID = String.valueOf(c.getString(c
                    .getColumnIndex("STAFF_ID")));

        }
        c.close();
        closeConnection();
        return clientID;

    }

    public String getName() {

        String clientID = "";
        openConnection();
        Cursor c = db
                .rawQuery(
                        "SELECT MEMBER_NAME FROM STAFF_DETAILS;",
                        null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            clientID = String.valueOf(c.getString(c
                    .getColumnIndex("MEMBER_NAME")));

        }
        c.close();
        closeConnection();
        return clientID;

    }

    public String getPhoneNumber() {

        String clientID = "";
        openConnection();
        Cursor c = db
                .rawQuery(
                        "SELECT MOBILE_NUMBER FROM MASTER_DATA;",
                        null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            clientID = String.valueOf(c.getString(c
                    .getColumnIndex("MOBILE_NUMBER")));

        }
        c.close();
        closeConnection();
        return clientID;

    }

    public String getChangePin() {

        String clientID = "";
        openConnection();
        Cursor c = db
                .rawQuery(
                        "SELECT CHANGE_PIN FROM MASTER_DATA;",
                        null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            clientID = String.valueOf(c.getString(c
                    .getColumnIndex("CHANGE_PIN")));

        }
        c.close();
        closeConnection();
        return clientID;

    }

    public void insertMasterData(MasterData _masterData) {
        openConnection();
        try {
            String qryString = "Insert into MASTER_DATA(MOBILE_NUMBER, CLIENT_ID, STAFF_ID, CHANGE_PIN) Values ('"
                    + _masterData.getMobileNo()
                    + "', '"
                    + _masterData.getClientID()
                    + "', '"
                    + _masterData.getStaffID()
                    + "', '"
                    + _masterData.getChnagePin() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public MasterData getMasterData() {
        openConnection();
        String query = "SELECT * FROM MASTER_DATA";
        Cursor c = db.rawQuery(query, null);
        MasterData masterData = new MasterData();
        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                masterData.setMobileNo(c.getString(c
                        .getColumnIndex("MOBILE_NUMBER")));
                masterData.setClientID(c.getString(c
                        .getColumnIndex("CLIENT_ID")));
                masterData.setStaffID(c.getString(c
                        .getColumnIndex("STAFF_ID")));
                masterData.setStaffID(c.getString(c
                        .getColumnIndex("CHANGE_PIN")));
            }
            c.close();
            closeConnection();
            return masterData;
        } else {
            c.close();
            closeConnection();
            return null;
        }
    }

    public void insertStaffDetails(MemberDetailsStaff _memberDetailsStaff) {
        openConnection();
        try {
            String qryString = "Insert into STAFF_DETAILS(CLIENT_ID, STAFF_ID, POLICY_REF, MEMBER_ID, MEMBER_NAME, RELATIONSHIP, GENDER, NATIONALITY, PHONE_NUMBER, EFFECTIVE_DATE, EMAIL) Values ('"
                    + _memberDetailsStaff.getClientID()
                    + "', '"
                    + _memberDetailsStaff.getStaffID()
                    + "', '"
                    + _memberDetailsStaff.getPolicyRef()
                    + "', '"
                    + _memberDetailsStaff.getMemberID()
                    + "', '"
                    + _memberDetailsStaff.getMemberName()
                    + "', '"
                    + _memberDetailsStaff.getRelationship()
                    + "', '"
                    + _memberDetailsStaff.getGender()
                    + "', '"
                    + _memberDetailsStaff.getNationality()
                    + "', '"
                    + _memberDetailsStaff.getPhoneNumber()
                    + "', '"
                    + _memberDetailsStaff.getEffectiveDate()
                    + "', '"
                    + _memberDetailsStaff.getEmail() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public MemberDetailsStaff getMemberDetailsStaff() {
        openConnection();
        String query = "SELECT * FROM STAFF_DETAILS";
        Cursor c = db.rawQuery(query, null);
        MemberDetailsStaff memberDetailsStaff = new MemberDetailsStaff();
        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                memberDetailsStaff.setClientID(c.getString(c
                        .getColumnIndex("CLIENT_ID")));
                memberDetailsStaff.setStaffID(c.getString(c
                        .getColumnIndex("STAFF_ID")));
                memberDetailsStaff.setPolicyRef(c.getString(c
                        .getColumnIndex("POLICY_REF")));
                memberDetailsStaff.setMemberID(c.getString(c
                        .getColumnIndex("MEMBER_ID")));
                memberDetailsStaff.setMemberName(c.getString(c
                        .getColumnIndex("MEMBER_NAME")));
                memberDetailsStaff.setRelationship(c.getString(c
                        .getColumnIndex("RELATIONSHIP")));
                memberDetailsStaff.setGender(c.getString(c
                        .getColumnIndex("GENDER")));
                memberDetailsStaff.setNationality(c.getString(c.
                        getColumnIndex("NATIONALITY")));
                memberDetailsStaff.setPhoneNumber(c.getString(c.
                        getColumnIndex("PHONE_NUMBER")));
                memberDetailsStaff.setEffectiveDate(c.getString(c.
                        getColumnIndex("EFFECTIVE_DATE")));
                memberDetailsStaff.setEmail(c.getString(c.
                        getColumnIndex("EMAIL")));
            }
            c.close();
            closeConnection();
            return memberDetailsStaff;
        } else {
            c.close();
            closeConnection();
            return null;
        }
    }

    public void insertDependentDetails(MemberDetailsChild _memberDetailsChild) {
        openConnection();
        try {
            String qryString = "Insert into DEPENDENT_DETAILS(CLIENT_ID, STAFF_ID, POLICY_REF, MEMBER_ID, MEMBER_NAME, RELATIONSHIP, GENDER, NATIONALITY, PHONE_NUMBER, EFFECTIVE_DATE, EMAIL) Values ('"
                    + _memberDetailsChild.getClientID()
                    + "', '"
                    + _memberDetailsChild.getStaffID()
                    + "', '"
                    + _memberDetailsChild.getPolicyRef()
                    + "', '"
                    + _memberDetailsChild.getMemberID()
                    + "', '"
                    + _memberDetailsChild.getMemberName()
                    + "', '"
                    + _memberDetailsChild.getRelationship()
                    + "', '"
                    + _memberDetailsChild.getGender()
                    + "', '"
                    + _memberDetailsChild.getNationality()
                    + "', '"
                    + _memberDetailsChild.getPhoneNumber()
                    + "', '"
                    + _memberDetailsChild.getEffectiveDate()
                    + "', '"
                    + _memberDetailsChild.getEmail() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public ArrayList<MemberDetailsChild> getMemberDetailsChild() {
        openConnection();
        String query = "SELECT * FROM DEPENDENT_DETAILS";
        Cursor c = db.rawQuery(query, null);

        ArrayList<MemberDetailsChild> memberDetailsChildList = new ArrayList<MemberDetailsChild>();

        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                do {
                    MemberDetailsChild memberDetailsChild = new MemberDetailsChild();
                    memberDetailsChild.setClientID(c.getString(c
                            .getColumnIndex("CLIENT_ID")));
                    memberDetailsChild.setStaffID(c.getString(c
                            .getColumnIndex("STAFF_ID")));
                    memberDetailsChild.setPolicyRef(c.getString(c
                            .getColumnIndex("POLICY_REF")));
                    memberDetailsChild.setMemberID(c.getString(c
                            .getColumnIndex("MEMBER_ID")));
                    memberDetailsChild.setMemberName(c.getString(c
                            .getColumnIndex("MEMBER_NAME")));
                    memberDetailsChild.setRelationship(c.getString(c
                            .getColumnIndex("RELATIONSHIP")));
                    memberDetailsChild.setGender(c.getString(c.
                            getColumnIndex("GENDER")));
                    memberDetailsChild.setNationality(c.getString(c.
                            getColumnIndex("NATIONALITY")));
                    memberDetailsChild.setPhoneNumber(c.getString(c.
                            getColumnIndex("PHONE_NUMBER")));
                    memberDetailsChild.setEffectiveDate(c.getString(c.
                            getColumnIndex("EFFECTIVE_DATE")));
                    memberDetailsChild.setEmail(c.getString(c.
                            getColumnIndex("EMAIL")));
                    memberDetailsChildList.add(memberDetailsChild);
                } while (c.moveToNext());
            }
        }
        c.close();
        closeConnection();
        return memberDetailsChildList;
    }

    public void insertPdfLinks(PdfLinks _pdfLinks) {
        openConnection();
        try {
            String qryString = "Insert into PDF_DETAILS(PDF_NAME, PDF_LINK) Values ('"
                    + _pdfLinks.getPdfName()
                    + "', '"
                    + _pdfLinks.getPdfLink() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public ArrayList<PdfLinks> getPdfLinks() {
        openConnection();
        String query = "SELECT * FROM PDF_DETAILS";
        Cursor c = db.rawQuery(query, null);

        ArrayList<PdfLinks> pdfLinksList = new ArrayList<PdfLinks>();

        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                do {
                    PdfLinks pdfLinks = new PdfLinks();
                    pdfLinks.setPdfName(c.getString(c
                            .getColumnIndex("PDF_NAME")));
                    pdfLinks.setPdfLink(c.getString(c
                            .getColumnIndex("PDF_LINK")));
                    pdfLinksList.add(pdfLinks);
                } while (c.moveToNext());
            }
        }
        c.close();
        closeConnection();
        return pdfLinksList;
    }

    public void insertClaimDetails(ClaimDetails _cliamStatus) {
        openConnection();
        try {
            String qryString = "Insert into CLAIM_DETAILS(CLAIM_NO, REGISTERED_DATE, MEMBER_NAME, STAFF_NAME, TREATMENT_DATE, AMOUNT, STATUS, " +
                    "MEMBER_TYPE, CLAIMED_AMOUNT, APPROVED_AMOUNT, EXCESS, DISALLOWANCE, SETTLED_AMOUNTRO, MODE_OF_PAYMENT, CHEQUE_NO, SETTLED_DATE, REMARKS, POLICY_NO, DIAGNOSIS) Values ('"
                    + _cliamStatus.getClaimNo()
                    + "', '"
                    + _cliamStatus.getRegisteredDate()
                    + "', '"
                    + _cliamStatus.getPatientName()
                    + "', '"
                    + _cliamStatus.getStaffName()
                    + "', '"
                    + _cliamStatus.getTreatmentDate()
                    + "', '"
                    + _cliamStatus.getSettledAmount()
                    + "', '"
                    + _cliamStatus.getStatus()
                    + "', '"
                    + _cliamStatus.getMemberType()
                    + "', '"
                    + _cliamStatus.getClaimedAmount()
                    + "', '"
                    + _cliamStatus.getApprovedAmount()
                    + "', '"
                    + _cliamStatus.getExcess()
                    + "', '"
                    + _cliamStatus.getDisallowance()
                    + "', '"
                    + _cliamStatus.getSettledAmountRO()
                    + "', '"
                    + _cliamStatus.getModeOfPayment()
                    + "', '"
                    + _cliamStatus.getChequeNo()
                    + "', '"
                    + _cliamStatus.getSettledAmount()
                    + "', '"
                    + _cliamStatus.getRemarks()
                    + "', '"
                    + _cliamStatus.getPolicyRefNo()
                    + "', '"
                    + _cliamStatus.getDiagnosis() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public ArrayList<ClaimDetails> getClaimStatus() {
        openConnection();
        String query = "SELECT * FROM CLAIM_DETAILS";
        Cursor c = db.rawQuery(query, null);

        ArrayList<ClaimDetails> claimStatusList = new ArrayList<ClaimDetails>();

        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                do {
                    ClaimDetails claimStatus = new ClaimDetails();
                    claimStatus.setClaimNo(c.getString(c
                            .getColumnIndex("CLAIM_NO")));
                    claimStatus.setRegisteredDate(c.getString(c
                            .getColumnIndex("REGISTERED_DATE")));
                    claimStatus.setPatientName(c.getString(c
                            .getColumnIndex("MEMBER_NAME")));
                    claimStatus.setStaffName(c.getString(c
                            .getColumnIndex("STAFF_NAME")));
                    claimStatus.setTreatmentDate(c.getString(c
                            .getColumnIndex("TREATMENT_DATE")));
                    claimStatus.setSettledAmount(c.getString(c
                            .getColumnIndex("AMOUNT")));
                    claimStatus.setStatus(c.getString(c
                            .getColumnIndex("STATUS")));
                    claimStatus.setMemberType(c.getString(c
                            .getColumnIndex("MEMBER_TYPE")));
                    claimStatus.setClaimedAmount(c.getString(c
                            .getColumnIndex("CLAIMED_AMOUNT")));
                    claimStatus.setApprovedAmount(c.getString(c
                            .getColumnIndex("APPROVED_AMOUNT")));
                    claimStatus.setExcess(c.getString(c
                            .getColumnIndex("EXCESS")));
                    claimStatus.setDisallowance(c.getString(c
                            .getColumnIndex("DISALLOWANCE")));
                    claimStatus.setSettledAmountRO(c.getString(c
                            .getColumnIndex("SETTLED_AMOUNTRO")));
                    claimStatus.setModeOfPayment(c.getString(c
                            .getColumnIndex("MODE_OF_PAYMENT")));
                    claimStatus.setChequeNo(c.getString(c
                            .getColumnIndex("CHEQUE_NO")));
                    claimStatus.setSettledAmount(c.getString(c
                            .getColumnIndex("SETTLED_DATE")));
                    claimStatus.setRemarks(c.getString(c
                            .getColumnIndex("REMARKS")));
                    claimStatus.setPolicyRefNo(c.getString(c
                            .getColumnIndex("POLICY_NO")));
                    claimStatus.setDiagnosis(c.getString(c
                            .getColumnIndex("DIAGNOSIS")));
                    claimStatusList.add(claimStatus);
                } while (c.moveToNext());
            }
        }
        c.close();
        closeConnection();
        return claimStatusList;
    }

    public void insertPreApproval(PreApproval _preApproval) {
        openConnection();
        try {
            String qryString = "Insert into PRE_APPROVAL(MEMBER_ID, PATIENT_NAME, STAFF_ID, STAFF_NAME, POLICY_REF, ENTRY_DATE, DIAGNOSIS, PALCE_CODE, HOSPITAL_NAME, PRE_APPROVAL_NO, PRE_APPROVAL_DATE, REMARKS, STATUS) Values ('"
                    + _preApproval.getPatientId()
                    + "', '"
                    + _preApproval.getPatientName()
                    + "', '"
                    + _preApproval.getStaffID()
                    + "', '"
                    + _preApproval.getStaffName()
                    + "', '"
                    + _preApproval.getPolicyRefNo()
                    + "', '"
                    + _preApproval.getEntryDate()
                    + "', '"
                    + _preApproval.getDiagnosis()
                    + "', '"
                    + _preApproval.getPlace()
                    + "', '"
                    + _preApproval.getHospitalName()
                    + "', '"
                    + _preApproval.getPreApprovalNo()
                    + "', '"
                    + _preApproval.getPreApprovalDate()
                    + "', '"
                    + _preApproval.getRemarks()
                    + "', '"
                    + _preApproval.getStatus() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public ArrayList<PreApproval> getPreApproval() {
        openConnection();
        String query = "SELECT * FROM PRE_APPROVAL";
        Cursor c = db.rawQuery(query, null);

        ArrayList<PreApproval> preApprovalList = new ArrayList<PreApproval>();

        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                do {
                    PreApproval preApproval = new PreApproval();
                    preApproval.setPatientId(c.getString(c
                            .getColumnIndex("MEMBER_ID")));
                    preApproval.setPatientName(c.getString(c
                            .getColumnIndex("PATIENT_NAME")));
                    preApproval.setStaffId(c.getString(c
                            .getColumnIndex("STAFF_ID")));
                    preApproval.setStaffName(c.getString(c
                            .getColumnIndex("STAFF_NAME")));
                    preApproval.setPolicyRefNo(c.getString(c
                            .getColumnIndex("POLICY_REF")));
                    preApproval.setEntryDate(c.getString(c
                            .getColumnIndex("ENTRY_DATE")));
                    preApproval.setDiagnosis(c.getString(c
                            .getColumnIndex("DIAGNOSIS")));
                    preApproval.setPlace(c.getString(c
                            .getColumnIndex("PALCE_CODE")));
                    preApproval.setHospitalName(c.getString(c
                            .getColumnIndex("HOSPITAL_NAME")));
                    preApproval.setPreApprovalNo(c.getString(c
                            .getColumnIndex("PRE_APPROVAL_NO")));
                    preApproval.setPreApprovalDate(c.getString(c
                            .getColumnIndex("PRE_APPROVAL_DATE")));
                    preApproval.setRemarks(c.getString(c
                            .getColumnIndex("REMARKS")));
                    preApproval.setStatus(c.getString(c
                            .getColumnIndex("STATUS")));
                    preApprovalList.add(preApproval);
                } while (c.moveToNext());
            }
        }
        c.close();
        closeConnection();
        return preApprovalList;
    }

    /**
     * End of EB Portal Table Transaction
     *
     */

    /**
     * Start of MonthlySalaryDeduction Table Transaction
     */

    public void clearMasterDetailsSalary() {
        openConnection();
        // To clear transaction Tables
        db.execSQL("DELETE FROM MASTER_DATA_SALARY");
        closeConnection();
    }

    public void clearPolicyDetais() {
        openConnection();
        // To clear transaction Tables
        db.execSQL("DELETE FROM STAFF_DETAILS_SALARY");
        db.execSQL("DELETE FROM STAFF_DETAILS_POLICY");
        db.execSQL("DELETE FROM ACCOUNT_SUMMARY");
        db.execSQL("DELETE FROM POLICY_INSTALLMENT");
        closeConnection();
    }

    public String getClientIdSalary() {

        String clientID = "";
        openConnection();
        Cursor c = db
                .rawQuery(
                        "SELECT CLIENT_ID FROM MASTER_DATA_SALARY;",
                        null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            clientID = String.valueOf(c.getString(c
                    .getColumnIndex("CLIENT_ID")));

        }
        c.close();
        closeConnection();
        return clientID;

    }

    public String getStaffIdSalary() {

        String clientID = "";
        openConnection();
        Cursor c = db
                .rawQuery(
                        "SELECT STAFF_ID FROM MASTER_DATA_SALARY;",
                        null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            clientID = String.valueOf(c.getString(c
                    .getColumnIndex("STAFF_ID")));

        }
        c.close();
        closeConnection();
        return clientID;

    }

    public String getNameSalary() {

        String clientID = "";
        openConnection();
        Cursor c = db
                .rawQuery(
                        "SELECT STAFF_NAME FROM STAFF_DETAILS_SALARY;",
                        null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            clientID = String.valueOf(c.getString(c
                    .getColumnIndex("STAFF_NAME")));

        }
        c.close();
        closeConnection();
        return clientID;

    }

    public String getNationalId() {

        String clientID = "";
        openConnection();
        Cursor c = db
                .rawQuery(
                        "SELECT NATIONAL_ID FROM STAFF_DETAILS_SALARY;",
                        null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            clientID = String.valueOf(c.getString(c
                    .getColumnIndex("NATIONAL_ID")));

        }
        c.close();
        closeConnection();
        return clientID;

    }

    public String getChangePinSalary() {

        String clientID = "";
        openConnection();
        Cursor c = db
                .rawQuery(
                        "SELECT CHANGE_PIN FROM MASTER_DATA_SALARY;",
                        null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            clientID = String.valueOf(c.getString(c
                    .getColumnIndex("CHANGE_PIN")));

        }
        c.close();
        closeConnection();
        return clientID;

    }

    public void insertMasterDataSalary(MasterDataSalary _masterDataSalary) {
        openConnection();
        try {
            String qryString = "Insert into MASTER_DATA_SALARY(NATIONAL_ID, CLIENT_ID, STAFF_ID, CHANGE_PIN) Values ('"
                    + _masterDataSalary.getNationalId()
                    + "', '"
                    + _masterDataSalary.getClientID()
                    + "', '"
                    + _masterDataSalary.getStaffID()
                    + "', '"
                    + _masterDataSalary.getChnagePin() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public MasterDataSalary getMasterDataSalary() {
        openConnection();
        String query = "SELECT * FROM MASTER_DATA_SALARY";
        Cursor c = db.rawQuery(query, null);
        MasterDataSalary masterDataSalary = new MasterDataSalary();
        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                masterDataSalary.setNationalId(c.getString(c
                        .getColumnIndex("NATIONAL_ID")));
                masterDataSalary.setClientID(c.getString(c
                        .getColumnIndex("CLIENT_ID")));
                masterDataSalary.setStaffID(c.getString(c
                        .getColumnIndex("STAFF_ID")));
                masterDataSalary.setStaffID(c.getString(c
                        .getColumnIndex("CHANGE_PIN")));
            }
            c.close();
            closeConnection();
            return masterDataSalary;
        } else {
            c.close();
            closeConnection();
            return null;
        }
    }

    public void insertStaffDetailsSalary(PolicyGroup _policyGroup) {
        openConnection();
        try {
            String qryString = "Insert into STAFF_DETAILS_SALARY(NATIONAL_ID, STAFF_ID, CLIENT_ID, STAFF_NAME) Values ('"
                    + _policyGroup.getNationalID()
                    + "', '"
                    + _policyGroup.getStaffId()
                    + "', '"
                    + _policyGroup.getclientId()
                    + "', '"
                    + _policyGroup.getName() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public PolicyGroup getMemberDetailsStaffSalary() {
        openConnection();
        String query = "SELECT * FROM STAFF_DETAILS_SALARY";
        Cursor c = db.rawQuery(query, null);
        PolicyGroup policyGroup = new PolicyGroup();
        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                policyGroup.setNationalID(c.getString(c
                        .getColumnIndex("NATIONAL_ID")));
                policyGroup.setStaffId(c.getString(c
                        .getColumnIndex("STAFF_ID")));
                policyGroup.setClientId(c.getString(c
                        .getColumnIndex("CLIENT_ID")));
                policyGroup.setName(c.getString(c
                        .getColumnIndex("STAFF_NAME")));
            }
            c.close();
            closeConnection();
            return policyGroup;
        } else {
            c.close();
            closeConnection();
            return null;
        }
    }

    public void insertStaffDetailsSalaryPolicy(PolicyChild _policyChild) {
        openConnection();
        try {
            String qryString = "Insert into STAFF_DETAILS_POLICY(NATIONAL_ID, STAFF_ID, POLICY_NO, START_DATE, END_DATE) Values ('"
                    + _policyChild.getNationalID()
                    + "', '"
                    + _policyChild.getStaffId()
                    + "', '"
                    + _policyChild.getNumber()
                    + "', '"
                    + _policyChild.getStartDate()
                    + "', '"
                    + _policyChild.getExpiryDate() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public ArrayList<PolicyChild> getMemberDetailsStaffSalaryPolicy() {
        openConnection();
        String query = "SELECT * FROM STAFF_DETAILS_POLICY";
        Cursor c = db.rawQuery(query, null);

        ArrayList<PolicyChild> policyChildList = new ArrayList<PolicyChild>();

        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                do {
                    PolicyChild policyChild = new PolicyChild();
                    policyChild.setNationalID(c.getString(c
                            .getColumnIndex("NATIONAL_ID")));
                    policyChild.setStaffId(c.getString(c
                            .getColumnIndex("STAFF_ID")));
                    policyChild.setNumber(c.getString(c
                            .getColumnIndex("POLICY_NO")));
                    policyChild.setStartDate(c.getString(c
                            .getColumnIndex("START_DATE")));
                    policyChild.setExpiryDate(c.getString(c
                            .getColumnIndex("END_DATE")));
                    policyChildList.add(policyChild);
                } while (c.moveToNext());
            }
        }
        c.close();
        closeConnection();
        return policyChildList;

    }

    public void insertAccountSummary(AccountSummary _accountSummary) {
        openConnection();
        try {
            String qryString = "Insert into ACCOUNT_SUMMARY(NATIONAL_ID, STAFF_ID, VEHICLE_NO, STATUS, OUTSTANDING_AMOUNT, TOTAL_PREMIUM, NEXT_INSTALLMENT_AMOUNT, INSURER, " +
                    "START_DATE, END_DATE, POLICY_NO, POLICY_YEAR) Values ('"
                    + _accountSummary.getNationalID()
                    + "', '"
                    + _accountSummary.getStaffId()
                    + "', '"
                    + _accountSummary.getVehicleNo()
                    + "', '"
                    + _accountSummary.getStatus()
                    + "', '"
                    + _accountSummary.getOutstandingAmount()
                    + "', '"
                    + _accountSummary.getTotalPremium()
                    + "', '"
                    + _accountSummary.getNextInstallmentAmount()
                    + "', '"
                    + _accountSummary.getInsurer()
                    + "', '"
                    + _accountSummary.getStartDate()
                    + "', '"
                    + _accountSummary.getEndDate()
                    + "', '"
                    + _accountSummary.getPolicyNo()
                    + "', '"
                    + _accountSummary.getPolicyYear() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public ArrayList<AccountSummary> getAccountSummary() {
        openConnection();
        String query = "SELECT * FROM ACCOUNT_SUMMARY";
        Cursor c = db.rawQuery(query, null);

        ArrayList<AccountSummary> accountSummaryArrayList = new ArrayList<AccountSummary>();
        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                do {
                    AccountSummary accountSummary = new AccountSummary();
                    accountSummary.setNationalID(c.getString(c
                            .getColumnIndex("NATIONAL_ID")));
                    accountSummary.setStaffId(c.getString(c
                            .getColumnIndex("STAFF_ID")));
                    accountSummary.setVehicleNo(c.getString(c
                            .getColumnIndex("VEHICLE_NO")));
                    accountSummary.setStatus(c.getString(c
                            .getColumnIndex("STATUS")));
                    accountSummary.setOutstandingAmount(c.getString(c
                            .getColumnIndex("OUTSTANDING_AMOUNT")));
                    accountSummary.setTotalPremium(c.getString(c
                            .getColumnIndex("TOTAL_PREMIUM")));
                    accountSummary.setNextInstallmentAmount(c.getString(c
                            .getColumnIndex("NEXT_INSTALLMENT_AMOUNT")));
                    accountSummary.setInsurer(c.getString(c
                            .getColumnIndex("INSURER")));
                    accountSummary.setStartDate(c.getString(c
                            .getColumnIndex("START_DATE")));
                    accountSummary.setEndDate(c.getString(c
                            .getColumnIndex("END_DATE")));
                    accountSummary.setPolicyNo(c.getString(c
                            .getColumnIndex("POLICY_NO")));
                    accountSummary.setPolicyYear(c.getString(c
                            .getColumnIndex("POLICY_YEAR")));
                    accountSummaryArrayList.add(accountSummary);
                } while (c.moveToNext());
            }
        }
        c.close();
        closeConnection();
        return accountSummaryArrayList;
    }

    public void insertInstallmentHistory(InstallmentHistory _installmentHistory) {
        openConnection();
        try {
            String qryString = "Insert into POLICY_INSTALLMENT(VEHICLE_NO, DATE, AMOUNT, STATUS) Values ('"
                    + _installmentHistory.getVehicleNo()
                    + "', '"
                    + _installmentHistory.getDate()
                    + "', '"
                    + _installmentHistory.getAmount()
                    + "', '"
                    + _installmentHistory.getStatus() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public ArrayList<InstallmentHistory> getInstallmentHistory(String VehicleNumber) {
        openConnection();
        String query = "SELECT * FROM POLICY_INSTALLMENT WHERE VEHICLE_NO = '" + VehicleNumber + "'";
        Cursor c = db.rawQuery(query, null);

        ArrayList<InstallmentHistory> installmentHistoryArrayList = new ArrayList<InstallmentHistory>();
        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                do {
                    InstallmentHistory installmentHistory = new InstallmentHistory();
                    installmentHistory.setVehicleNo(c.getString(c
                            .getColumnIndex("VEHICLE_NO")));
                    installmentHistory.setDate(c.getString(c
                            .getColumnIndex("DATE")));
                    installmentHistory.setAmount(c.getString(c
                            .getColumnIndex("AMOUNT")));
                    installmentHistory.setStatus(c.getString(c
                            .getColumnIndex("STATUS")));
                    installmentHistoryArrayList.add(installmentHistory);
                } while (c.moveToNext());
            }
        }
            c.close();
            closeConnection();
            return installmentHistoryArrayList;
    }

    /**
     * End of MonthlySalaryDeduction Table Transaction
     *
     */

    /**
     * Start of PersonalLines Table Transaction
     *
     */

    public void clearMasterDetailsLines() {
        openConnection();
        // To clear transaction Tables
        db.execSQL("DELETE FROM MASTER_DATA_LINES");
        closeConnection();
    }

    public void clearPolicyDetaisLines() {
        openConnection();
        // To clear transaction Tables
        db.execSQL("DELETE FROM STAFF_DETAILS_LINES");
        db.execSQL("DELETE FROM STAFF_DETAILS_POLICY_LINES");
        db.execSQL("DELETE FROM ACCOUNT_SUMMARY_LINES");
        closeConnection();
    }

    public String getClientIdLines() {

        String clientID = "";
        openConnection();
        Cursor c = db
                .rawQuery(
                        "SELECT CLIENT_ID FROM MASTER_DATA_LINES;",
                        null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            clientID = String.valueOf(c.getString(c
                    .getColumnIndex("CLIENT_ID")));

        }
        c.close();
        closeConnection();
        return clientID;

    }

    public String getStaffIdLines() {

        String clientID = "";
        openConnection();
        Cursor c = db
                .rawQuery(
                        "SELECT STAFF_ID FROM MASTER_DATA_LINES;",
                        null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            clientID = String.valueOf(c.getString(c
                    .getColumnIndex("STAFF_ID")));

        }
        c.close();
        closeConnection();
        return clientID;

    }

    public String getNameLines() {

        String clientID = "";
        openConnection();
        Cursor c = db
                .rawQuery(
                        "SELECT STAFF_NAME FROM STAFF_DETAILS_LINES;",
                        null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            clientID = String.valueOf(c.getString(c
                    .getColumnIndex("STAFF_NAME")));

        }
        c.close();
        closeConnection();
        return clientID;

    }

    public String getNationalIdLines() {

        String clientID = "";
        openConnection();
        Cursor c = db
                .rawQuery(
                        "SELECT NATIONAL_ID FROM STAFF_DETAILS_LINES;",
                        null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            clientID = String.valueOf(c.getString(c
                    .getColumnIndex("NATIONAL_ID")));

        }
        c.close();
        closeConnection();
        return clientID;

    }

    public String getChangePinLines() {

        String clientID = "";
        openConnection();
        Cursor c = db
                .rawQuery(
                        "SELECT CHANGE_PIN FROM MASTER_DATA_LINES;",
                        null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            clientID = String.valueOf(c.getString(c
                    .getColumnIndex("CHANGE_PIN")));

        }
        c.close();
        closeConnection();
        return clientID;

    }

    public void insertMasterDataLines(MasterDataLines _masterDataLines) {
        openConnection();
        try {
            String qryString = "Insert into MASTER_DATA_LINES(NATIONAL_ID, CLIENT_ID, STAFF_ID, CHANGE_PIN) Values ('"
                    + _masterDataLines.getNationalId()
                    + "', '"
                    + _masterDataLines.getClientID()
                    + "', '"
                    + _masterDataLines.getStaffID()
                    + "', '"
                    + _masterDataLines.getChnagePin() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public MasterDataLines getMasterDataLines() {
        openConnection();
        String query = "SELECT * FROM MASTER_DATA_LINES";
        Cursor c = db.rawQuery(query, null);
        MasterDataLines masterDataLines = new MasterDataLines();
        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                masterDataLines.setNationalId(c.getString(c
                        .getColumnIndex("NATIONAL_ID")));
                masterDataLines.setClientID(c.getString(c
                        .getColumnIndex("CLIENT_ID")));
                masterDataLines.setStaffID(c.getString(c
                        .getColumnIndex("STAFF_ID")));
                masterDataLines.setStaffID(c.getString(c
                        .getColumnIndex("CHANGE_PIN")));
            }
            c.close();
            closeConnection();
            return masterDataLines;
        }else{
            c.close();
            closeConnection();
            return null;
        }
    }

    public void insertStaffDetailsLines(PolicyLineGroup _policyLineGroup) {
        openConnection();
        try {
            String qryString = "Insert into STAFF_DETAILS_LINES(NATIONAL_ID, STAFF_ID, CLIENT_ID, STAFF_NAME) Values ('"
                    + _policyLineGroup.getNationalID()
                    + "', '"
                    + _policyLineGroup.getStaffId()
                    + "', '"
                    + _policyLineGroup.getclientId()
                    + "', '"
                    + _policyLineGroup.getName() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public PolicyLineGroup getMemberDetailsStaffLines() {
        openConnection();
        String query = "SELECT * FROM STAFF_DETAILS_LINES";
        Cursor c = db.rawQuery(query, null);
        PolicyLineGroup policyLineGroup = new PolicyLineGroup();
        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                policyLineGroup.setNationalID(c.getString(c
                        .getColumnIndex("NATIONAL_ID")));
                policyLineGroup.setStaffId(c.getString(c
                        .getColumnIndex("STAFF_ID")));
                policyLineGroup.setClientId(c.getString(c
                        .getColumnIndex("CLIENT_ID")));
                policyLineGroup.setName(c.getString(c
                        .getColumnIndex("STAFF_NAME")));
            }
            c.close();
            closeConnection();
            return policyLineGroup;
        }else{
            c.close();
            closeConnection();
            return null;
        }
    }

    public void insertStaffDetailsLinesPolicy(PolicyLineChild _policyLineChild) {
        openConnection();
        try {
            String qryString = "Insert into STAFF_DETAILS_POLICY_LINES(NATIONAL_ID, STAFF_ID, POLICY_NO, START_DATE, END_DATE) Values ('"
                    + _policyLineChild.getNationalID()
                    + "', '"
                    + _policyLineChild.getStaffId()
                    + "', '"
                    + _policyLineChild.getNumber()
                    + "', '"
                    + _policyLineChild.getStartDate()
                    + "', '"
                    + _policyLineChild.getExpiryDate() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public ArrayList<PolicyLineChild> getMemberDetailsStaffLinesPolicy() {
        openConnection();
        String query = "SELECT * FROM STAFF_DETAILS_POLICY_LINES";
        Cursor c = db.rawQuery(query, null);

        ArrayList<PolicyLineChild> policyLineChildList = new ArrayList<PolicyLineChild>();

        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                do {
                    PolicyLineChild policyLineChild = new PolicyLineChild();
                    policyLineChild.setNationalID(c.getString(c
                            .getColumnIndex("NATIONAL_ID")));
                    policyLineChild.setStaffId(c.getString(c
                            .getColumnIndex("STAFF_ID")));
                    policyLineChild.setNumber(c.getString(c
                            .getColumnIndex("POLICY_NO")));
                    policyLineChild.setStartDate(c.getString(c
                            .getColumnIndex("START_DATE")));
                    policyLineChild.setExpiryDate(c.getString(c
                            .getColumnIndex("END_DATE")));
                    policyLineChildList.add(policyLineChild);
                } while (c.moveToNext());
            }
        }
            c.close();
            closeConnection();
            return policyLineChildList;
    }

    public void insertAccountSummaryLines(AccountSummaryLines _accountSummaryLines) {
        openConnection();
        try {
            String qryString = "Insert into ACCOUNT_SUMMARY_LINES(NATIONAL_ID, STAFF_ID, VEHICLE_NO, STATUS, OUTSTANDING_AMOUNT, TOTAL_PREMIUM, NEXT_INSTALLMENT_AMOUNT, INSURER, " +
                    "START_DATE, END_DATE, POLICY_NO, POLICY_YEAR) Values ('"
                    + _accountSummaryLines.getNationalID()
                    + "', '"
                    + _accountSummaryLines.getStaffId()
                    + "', '"
                    + _accountSummaryLines.getVehicleNo()
                    + "', '"
                    + _accountSummaryLines.getStatus()
                    + "', '"
                    + _accountSummaryLines.getOutstandingAmount()
                    + "', '"
                    + _accountSummaryLines.getTotalPremium()
                    + "', '"
                    + _accountSummaryLines.getNextInstallmentAmount()
                    + "', '"
                    + _accountSummaryLines.getInsurer()
                    + "', '"
                    + _accountSummaryLines.getStartDate()
                    + "', '"
                    + _accountSummaryLines.getEndDate()
                    + "', '"
                    + _accountSummaryLines.getPolicyNo()
                    + "', '"
                    + _accountSummaryLines.getPolicyYear() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public ArrayList<AccountSummaryLines> getAccountSummaryLines() {
        openConnection();
        String query = "SELECT * FROM ACCOUNT_SUMMARY_LINES";
        Cursor c = db.rawQuery(query, null);

        ArrayList<AccountSummaryLines> accountSummaryLinesArrayList = new ArrayList<AccountSummaryLines>();
        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                do {
                    AccountSummaryLines accountLinesSummary = new AccountSummaryLines();
                    accountLinesSummary.setNationalID(c.getString(c
                            .getColumnIndex("NATIONAL_ID")));
                    accountLinesSummary.setStaffId(c.getString(c
                            .getColumnIndex("STAFF_ID")));
                    accountLinesSummary.setVehicleNo(c.getString(c
                            .getColumnIndex("VEHICLE_NO")));
                    accountLinesSummary.setStatus(c.getString(c
                            .getColumnIndex("STATUS")));
                    accountLinesSummary.setOutstandingAmount(c.getString(c
                            .getColumnIndex("OUTSTANDING_AMOUNT")));
                    accountLinesSummary.setTotalPremium(c.getString(c
                            .getColumnIndex("TOTAL_PREMIUM")));
                    accountLinesSummary.setNextInstallmentAmount(c.getString(c
                            .getColumnIndex("NEXT_INSTALLMENT_AMOUNT")));
                    accountLinesSummary.setInsurer(c.getString(c
                            .getColumnIndex("INSURER")));
                    accountLinesSummary.setStartDate(c.getString(c
                            .getColumnIndex("START_DATE")));
                    accountLinesSummary.setEndDate(c.getString(c
                            .getColumnIndex("END_DATE")));
                    accountLinesSummary.setPolicyNo(c.getString(c
                            .getColumnIndex("POLICY_NO")));
                    accountLinesSummary.setPolicyYear(c.getString(c
                            .getColumnIndex("POLICY_YEAR")));
                    accountSummaryLinesArrayList.add(accountLinesSummary);
                } while (c.moveToNext());
            }
        }
            c.close();
            closeConnection();
            return accountSummaryLinesArrayList;
    }

    /**
     * End of PersonalLines Table Transaction
     *
     */

    /**
     * Start of ContactDetails Table Transaction
     *
     */

    public void clearContactInfo() {
        openConnection();
        // To clear transaction Tables
        db.execSQL("DELETE FROM CONTACT_COUNTRY");
        db.execSQL("DELETE FROM CONTACT_DETAILS");
        closeConnection();
    }

    public int getContactLocationCount() {
        openConnection();
        String countQuery = "SELECT ID FROM CONTACT_COUNTRY";
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        closeConnection();
        return cnt;
    }

    public int getContactDetailsCount(int index) {
        openConnection();
        String countQuery = "SELECT BRANCH_NAME FROM CONTACT_DETAILS WHERE INDEXS = " + index;
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        closeConnection();
        return cnt;
    }

    public void insertContactLocation(ContactLocation _contactLocation) {
        openConnection();
        try {
            String qryString = "Insert into CONTACT_COUNTRY(ID, COUNTRY) Values ('"
                    + _contactLocation.getID()
                    + "', '"
                    + _contactLocation.getLocation() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public ContactLocation getContactLocation(int index) {
        openConnection();
        String query = "SELECT * FROM CONTACT_COUNTRY WHERE ID = " + index;
        Cursor c = db.rawQuery(query, null);
        ContactLocation contactLocation1 = new ContactLocation();
        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                contactLocation1.setID(c.getString(c.
                        getColumnIndex("ID")));
                contactLocation1.setLocation(c.getString(c.
                        getColumnIndex("COUNTRY")));
            }
            c.close();
            closeConnection();
            return contactLocation1;
        }else{
            c.close();
            closeConnection();
            return null;
        }
    }

    public void insertContactDetails(ContactDetails _contactDetails) {
        openConnection();
        try {
            String qryString = "Insert into CONTACT_DETAILS(ID, INDEXS, BRANCH_NAME, ATTENTION, P_O_BOX, POSTAL_CODE, ADDRESS, TELEPHONE, MOBILE, FAX, EMAIL, HOT_LINE, DIRECT_LINE) Values ('"
                    + _contactDetails.getID()
                    + "', '"
                    + _contactDetails.getIndex()
                    + "', '"
                    + _contactDetails.getBranchName()
                    + "', '"
                    + _contactDetails.getattention()
                    + "', '"
                    + _contactDetails.getPO_Box()
                    + "', '"
                    + _contactDetails.getPostalCode()
                    + "', '"
                    + _contactDetails.getAddress()
                    + "', '"
                    + _contactDetails.getTelephone()
                    + "', '"
                    + _contactDetails.getMobile()
                    + "', '"
                    + _contactDetails.getFax()
                    + "', '"
                    + _contactDetails.getEmail()
                    + "', '"
                    + _contactDetails.getHot_lines()
                    + "', '"
                    + _contactDetails.getDirect_lines() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public ContactDetails getContactDetails(String id, String index) {
        openConnection();
        String query = "SELECT * FROM CONTACT_DETAILS WHERE ID = " + id + " AND INDEXS = " + index;
        Cursor c = db.rawQuery(query, null);
        ContactDetails contactDetails = new ContactDetails();
        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                contactDetails.setID(c.getString(c
                        .getColumnIndex("ID")));
                contactDetails.setIndex(c.getString(c
                        .getColumnIndex("INDEXS")));
                contactDetails.setBranchName(c.getString(c
                        .getColumnIndex("BRANCH_NAME")));
                contactDetails.setAttention(c.getString(c
                        .getColumnIndex("ATTENTION")));
                contactDetails.setPO_Box(c.getString(c
                        .getColumnIndex("P_O_BOX")));
                contactDetails.setPostalCode(c.getString(c
                        .getColumnIndex("POSTAL_CODE")));
                contactDetails.setAddress(c.getString(c
                        .getColumnIndex("ADDRESS")));
                contactDetails.setTelephone(c.getString(c
                        .getColumnIndex("TELEPHONE")));
                contactDetails.setMobile(c.getString(c
                        .getColumnIndex("MOBILE")));
                contactDetails.setFax(c.getString(c.
                        getColumnIndex("FAX")));
                contactDetails.setEmail(c.getString(c.
                        getColumnIndex("EMAIL")));
                contactDetails.setHotLine(c.getString(c.
                        getColumnIndex("HOT_LINE")));
                contactDetails.setDirect_lines(c.getString(c.
                        getColumnIndex("DIRECT_LINE")));
            }
            c.close();
            closeConnection();
            return contactDetails;
        }else{
            c.close();
            closeConnection();
            return null;
        }
    }

    /**
     * End of ContactDetails Table Transaction
     *
     */

    /**
     * Start of ContactDetailsAr Table Transaction
     *
     */

    public void clearContactInfoAr() {
        openConnection();
        // To clear transaction Tables
        db.execSQL("DELETE FROM CONTACT_COUNTRY_AR");
        db.execSQL("DELETE FROM CONTACT_DETAILS_AR");
        closeConnection();
    }

    public int getContactLocationCountAr() {
        openConnection();
        String countQuery = "SELECT ID_AR FROM CONTACT_COUNTRY_AR";
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        closeConnection();
        return cnt;
    }

    public int getContactDetailsCountAr(int index) {
        openConnection();
        String countQuery = "SELECT BRANCH_NAME_AR FROM CONTACT_DETAILS_AR WHERE INDEXS_AR = " + index;
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        closeConnection();
        return cnt;
    }

    public void insertContactLocationAr(ContactLocationAr _contactLocationAr) {
        openConnection();
        try {
            String qryString = "Insert into CONTACT_COUNTRY_AR(ID_AR, COUNTRY_AR) Values ('"
                    + _contactLocationAr.getID_ar()
                    + "', '"
                    + _contactLocationAr.getLocation_ar() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public ContactLocationAr getContactLocationAr(int index) {
        openConnection();
        String query = "SELECT * FROM CONTACT_COUNTRY_AR WHERE ID_AR = " + index;
        Cursor c = db.rawQuery(query, null);
        ContactLocationAr contactLocationAr1 = new ContactLocationAr();
        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                contactLocationAr1.setID_ar(c.getString(c.
                        getColumnIndex("ID_AR")));
                contactLocationAr1.setLocation_ar(c.getString(c.
                        getColumnIndex("COUNTRY_AR")));
            }
            c.close();
            closeConnection();
            return contactLocationAr1;
        }else{
            c.close();
            closeConnection();
            return null;
        }
    }

    public void insertContactDetailsAr(ContactDetailsAr _contactDetailsAr) {
        openConnection();
        try {
            String qryString = "Insert into CONTACT_DETAILS_AR(ID_AR, INDEXS_AR, BRANCH_NAME_AR, ATTENTION_AR, P_O_BOX_AR, POSTAL_CODE_AR, ADDRESS_AR, TELEPHONE_AR, MOBILE_AR, " +
                    "FAX_AR, EMAIL_AR, HOT_LINE_AR) Values ('"
                    + _contactDetailsAr.getID_ar()
                    + "', '"
                    + _contactDetailsAr.getIndex_ar()
                    + "', '"
                    + _contactDetailsAr.getBranchName_ar()
                    + "', '"
                    + _contactDetailsAr.getattention_ar()
                    + "', '"
                    + _contactDetailsAr.getPO_Box_ar()
                    + "', '"
                    + _contactDetailsAr.getPostalCode_ar()
                    + "', '"
                    + _contactDetailsAr.getAddress_ar()
                    + "', '"
                    + _contactDetailsAr.getTelephone_ar()
                    + "', '"
                    + _contactDetailsAr.getMobileAr()
                    + "', '"
                    + _contactDetailsAr.getFax_ar()
                    + "', '"
                    + _contactDetailsAr.getEmail_ar()
                    + "', '"
                    + _contactDetailsAr.getHot_lines_ar() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public ContactDetailsAr getContactDetailsAr(String id, String index) {
        openConnection();
        String query = "SELECT * FROM CONTACT_DETAILS_AR WHERE ID_AR = " + id + " AND INDEXS_AR = " + index;
        Cursor c = db.rawQuery(query, null);
        ContactDetailsAr contactDetailsAr = new ContactDetailsAr();
        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                contactDetailsAr.setID_ar(c.getString(c
                        .getColumnIndex("ID_AR")));
                contactDetailsAr.setIndex_ar(c.getString(c
                        .getColumnIndex("INDEXS_AR")));
                contactDetailsAr.setBranchName_ar(c.getString(c
                        .getColumnIndex("BRANCH_NAME_AR")));
                contactDetailsAr.setAttention_ar(c.getString(c
                        .getColumnIndex("ATTENTION_AR")));
                contactDetailsAr.setPO_Box_ar(c.getString(c
                        .getColumnIndex("P_O_BOX_AR")));
                contactDetailsAr.setPostalCode_ar(c.getString(c
                        .getColumnIndex("POSTAL_CODE_AR")));
                contactDetailsAr.setAddress_ar(c.getString(c
                        .getColumnIndex("ADDRESS_AR")));
                contactDetailsAr.setTelephone_ar(c.getString(c
                        .getColumnIndex("TELEPHONE_AR")));
                contactDetailsAr.setMobileAr(c.getString(c
                        .getColumnIndex("MOBILE_AR")));
                contactDetailsAr.setFax_ar(c.getString(c.
                        getColumnIndex("FAX_AR")));
                contactDetailsAr.setEmail_ar(c.getString(c.
                        getColumnIndex("EMAIL_AR")));
                contactDetailsAr.setHotLine_ar(c.getString(c.
                        getColumnIndex("HOT_LINE_AR")));
            }
            c.close();
            closeConnection();
            return contactDetailsAr;
        }else{
            c.close();
            closeConnection();
            return null;
        }
    }

    /**
     * End of ContactDetails Table Transaction
     *
     */

    // Sqlite DB Helper Class
    private static class RMSDataHelper extends SQLiteOpenHelper {
        RMSDataHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
