package com.tcb.scf;

import com.tcb.auto.subprocess.webservice.SOAPClient;
import com.tcb.auto.subprocess.xml.XmlDriver;
import com.tcb.auto.utils.Commons;
import com.tcb.auto.utils.GlobalVariable;


import java.util.*;

/**
 * author: yennth28
 */
public class WebServiceClient {
    private String endPointCreateLoan;
    private String endPointUpdateLoan;
    private String usernameWS;
    private String passwordWS;

    public WebServiceClient() {
       Properties properties = GlobalVariable.listConfigEnv;
       if (properties == null) {
           properties = GlobalVariable.loadEnvConfigs();
       }
        usernameWS = properties.getProperty("USERNAME.WS");
        passwordWS = properties.getProperty("PASS.WS");
        endPointCreateLoan = properties.getProperty("CREATE.LOAN.ENDPOINT");
        endPointUpdateLoan = properties.getProperty("UPDATE.LOAN.ENDPOINT");
    }

    /**
     * Create new Loan
     * @return loan key
     */
  /*  public String createNewLoan(String externalID,String maturityDate,String branchCode,String branchName,
            String customerID,String amount,String valueDate,String LiqAcct,String drawdownAccount,String loanCurrency){

       String messageRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"http://www.techcombank.com.vn/Core/Banking/V1\">\n" +
               "   <soapenv:Header/>\n" +
               "  <soapenv:Body xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
               "\t<AddLoanReq xmlns=\"http://www.techcombank.com.vn/Core/Banking/V1\">\n" +
               "\t\t<MsgReqHdr>\n" +
               "\t\t\t<TxId xmlns:tns=\"http://www.techcombank.com.vn/Core/Banking/V1\">cba573b5-b5ce-4f10-b02c-fb9f4f4298cb</TxId>\n" +
               "\t\t\t<AppHdr>\n" +
               "\t\t\t\t<Fr>\n" +
               "\t\t\t\t\t<OrgId>\n" +
               "\t\t\t\t\t\t<Nm>ECM</Nm>\n" +
               "\t\t\t\t\t</OrgId>\n" +
               "\t\t\t\t</Fr>\n" +
               "\t\t\t\t<To>\n" +
               "\t\t\t\t\t<OrgId>\n" +
               "\t\t\t\t\t\t<Nm>T24</Nm>\n" +
               "\t\t\t\t\t</OrgId>\n" +
               "\t\t\t\t</To>\n" +
               "\t\t\t\t<BizMsgIdr>ECM</BizMsgIdr>\n" +
               "\t\t\t\t<MsgDefIdr>LOANS</MsgDefIdr>\n" +
               "\t\t\t\t<BizSvc>createLoan</BizSvc>\n" +
               "\t\t\t\t<CreDt>2020-06-25T08:09:09.361Z</CreDt>\n" +
               "\t\t\t</AppHdr>\n" +
               "\t\t\t<Cstm/>\n" +
               "\t\t</MsgReqHdr>\n" +
               "\t\t<MessageId>3674434305</MessageId>\n" +
               "\t\t<Document>\n" +
               "\t\t\t<MsgType>M004</MsgType>\n" +
               "\t\t\t<UseOfLoan>000400</UseOfLoan>\n" +
               "\t\t\t<SubCategory>2010</SubCategory>\n" +
               "\t\t\t<ProductCd>110208</ProductCd>\n" +
               "\t\t\t<CustomerId>"+customerID+"</CustomerId>\n" +
               "\t\t\t<Currency>"+loanCurrency+"</Currency>\n" +
               "\t\t\t<Principle>"+amount+"</Principle>\n" +
               "\t\t\t<ValueDate>"+valueDate+"</ValueDate>\n" +
               "\t\t\t<MaturityDate>"+maturityDate+"</MaturityDate>\n" +
               "\t\t\t<IntRateType>1</IntRateType>\n" +
               "\t\t\t<IntRate>6</IntRate>\n" +
               "\t\t\t<Description>GN KU NA Case:"+externalID+"</Description>\n" +
               "\t\t\t<IsSecured>NO</IsSecured>\n" +
               "\t\t\t<ScroringId>NA</ScroringId>\n" +
               "\t\t\t<TcbDept>1111600</TcbDept>\n" +
               "\t\t\t<ApproverList>NA</ApproverList>\n" +
               "\t\t\t<PrinLiqAcct>"+LiqAcct+"</PrinLiqAcct>\n" +
               "\t\t\t<IntLiqAcct>"+LiqAcct+"</IntLiqAcct>\n" +
               "\t\t\t<DrawdownAccount>"+drawdownAccount+"</DrawdownAccount>\n" +
               "\t\t\t<ExternalAppID>"+externalID+"</ExternalAppID>\n" +
               "\t\t\t<Branch>\n" +
               "\t\t\t\t<Cd>"+branchCode+"</Cd>\n" +
               "\t\t\t\t<Mne>"+branchName+"</Mne>\n" +
               "\t\t\t</Branch>\n" +
               "\t\t\t<DeptAcctOfficer>110016</DeptAcctOfficer>\n" +
               "\t\t\t<BasicPeriodicSpread>0</BasicPeriodicSpread>\n" +
               "\t\t\t<AutoScheds>YES</AutoScheds>\n" +
               "\t\t\t<LegacyRef>NA</LegacyRef>\n" +
               "\t\t\t<Custom>\n" +
               "\t\t\t\t<RiskAppetiteType>03</RiskAppetiteType>\n" +
               "\t\t\t\t<DefineScheds>NO</DefineScheds>\n" +
               "\t\t\t\t<Narrative>NTEST5</Narrative>\n" +
               "\t\t\t\t<SubCate>APFS</SubCate>\n" +
               "\t\t\t</Custom>\n" +
               "\t\t</Document>\n" +
               "\t</AddLoanReq>\n" +
               "</soapenv:Body>\n" +
               "</soapenv:Envelope>";

        Commons.getLogger().debug("Send request: " + messageRequest);
        SOAPClient soapClient = new SOAPClient();
        try {
           String response = soapClient.requestHTTP(endPointCreateLoan, messageRequest, "xml", usernameWS, passwordWS);
            Commons.getLogger().debug("Response call ws: " + response);
            List<String> nodes = Arrays.asList("v1:Status,v1:LoanKey".split(","));
            XmlDriver xmlDriver = new XmlDriver();
            Map<String, String> listNodes = xmlDriver.getNodeValues(response, nodes);
            String responeCode = listNodes.get("v1:Status");
            if(responeCode.equalsIgnoreCase("0")){
                String loanID = listNodes.get("v1:LoanKey");
                return loanID;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
       return null;
    }
*/
    /**
     * Create new Loan
     * @return loan key
     */
    public String createNewLoan(String externalID,String maturityDate,String branchCode,String branchName,
                                String customerID,String amount,String valueDate,String LiqAcct,String drawdownAccount,String loanCurrency){

        String messageRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wsac=\"http://temenos.com/WSACHPayments\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <wsac:callOfs>\n" +
                "         <!--Optional:-->\n" +
                "         <!--Automation Tester - OnT:-->\n" +
                "         <OfsRequest>LD.LOANS.AND.DEPOSITS,LN1.TCB/I/PROCESS,OSBTBP/P@ssw0rd/,,CONT.PURPOSE=2010,INDUSTRY.TCB=000100,CONT.PRODUCT=120101,CUSTOMER.ID="+customerID+",CURRENCY="+loanCurrency+",AMOUNT="+amount+",VALUE.DATE="+valueDate+",FIN.MAT.DATE="+maturityDate+
                ",GLTENOR=ST,INTEREST.RATE=10,LOAI.KVRR=03,SECURED..Y.N=NO,VAYMON=Y,AUTO.SCHEDS=NO,DEFINE.SCHEDS=NO,SO.YCBH=OTHER,NIEN.KIM=NO,VIP.CUST=NO,MAP.QCA=YES,HD.XEP.HANG=NA,LEGACY.REF=77625,MIS.ACCT.OFFICER=110016,TCB.DEPARTMENT=1111600," +
                "OFFICER.PAY.AC=13825274540018,DS.PHE.DUYET=AAA,PRIN.LIQ.ACCT="+LiqAcct+",INT.LIQ.ACCT="+LiqAcct+",DRAWDOWN.ACCOUNT="+drawdownAccount+",CHECK.DUP.HS:1:1="+externalID+"</OfsRequest>\n" +
                "      </wsac:callOfs>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        Commons.getLogger().debug("Send request: " + messageRequest);
        System.out.println(messageRequest);
        SOAPClient soapClient = new SOAPClient();
        try {
            String response = soapClient.requestHTTP(endPointCreateLoan, messageRequest,"POST", "xml", usernameWS, passwordWS);
            System.out.println("response: "+response);
            Commons.getLogger().debug("Response call ws: " + response);
            List<String> nodes = Arrays.asList("successIndicator,transactionId,messages".split(","));
            XmlDriver xmlDriver = new XmlDriver();
            Map<String, String> listNodes = xmlDriver.getNodeValues(response, nodes);
            String responeCode = listNodes.get("successIndicator");
            String loanID = listNodes.get("transactionId");

            if(responeCode.equalsIgnoreCase("Success")){
                Commons.log4jAndReport("LD: ",loanID);
                return loanID;
            }else {
                Commons.log4jAndReport("LD: ",loanID);
                Commons.log4jAndReport(responeCode);
                String message = listNodes.get("messages");
                Commons.log4jAndReport("Error",message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * Approval LD in T24
     * YenNTH28
     * @return
     */

    public boolean approvalLoan(String ld){

        String messageRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wsac=\"http://temenos.com/WSACHPayments\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <wsac:callOfs>\n" +
                "         <OfsRequest>LD.LOANS.AND.DEPOSITS,LN1.TCB/A/PROCESS,BAODT01/1234ABCd/,"+ld+",</OfsRequest>\n" +
                "      </wsac:callOfs>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        Commons.getLogger().debug("Send request: " + messageRequest);
        SOAPClient soapClient = new SOAPClient();
        try {
            String response = soapClient.requestHTTP(endPointCreateLoan, messageRequest,"POST", "xml", usernameWS, passwordWS);
            //System.out.println("Response: " + response);
            Commons.getLogger().debug("Response call ws: " + response);
            List<String> nodes = Arrays.asList("successIndicator,transactionId".split(","));
            XmlDriver xmlDriver = new XmlDriver();
            Map<String, String> listNodes = xmlDriver.getNodeValues(response, nodes);
            String responeCode = listNodes.get("successIndicator");
            if(responeCode.equalsIgnoreCase("Success")){

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    /**
     * update status create loan from ECM to SCF
     */
    public boolean updateLoanStatus(String momitorID, String ackID, String LD,String valueDate,String maturityDate,
                                    String loanAmount,String customerID,String drawdownAccount,String loanCurrency){

        String messageRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://www.techcombank.com/entity/global/vn/corporateloan/loanstatusupdate/1.0\" xmlns:urn=\"urn:iso:std:iso:20022:tech:xsd:head.001.001.01\" xmlns:ns1=\"http://www.techcombank.com/entity/global/vn/common/datatype/1.0\" xmlns:ns2=\"http://www.techcombank.com/entity/global/vn/loan/corporateloaninfo/1.0\">\n" +
                "   <soapenv:Header/>\n" +
                "  <soapenv:Body xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "\t<LoanInitiateStatusUpdateReq xmlns=\"http://www.techcombank.com/entity/global/vn/corporateloan/loanstatusupdate/1.0\">\n" +
                "\t\t<ns2:AppHdr xmlns:ns2=\"urn:iso:std:iso:20022:tech:xsd:head.001.001.01\">\n" +
                "\t\t\t<ns2:CharSet>UTF-8</ns2:CharSet>\n" +
                "\t\t\t<ns2:Fr>\n" +
                "\t\t\t\t<ns2:OrgId>\n" +
                "\t\t\t\t\t<ns2:Nm>ECM</ns2:Nm>\n" +
                "\t\t\t\t</ns2:OrgId>\n" +
                "\t\t\t</ns2:Fr>\n" +
                "\t\t\t<ns2:To>\n" +
                "\t\t\t\t<ns2:OrgId>\n" +
                "\t\t\t\t\t<ns2:Nm>SCF</ns2:Nm>\n" +
                "\t\t\t\t</ns2:OrgId>\n" +
                "\t\t\t</ns2:To>\n" +
                "\t\t\t<ns2:BizMsgIdr>SCF</ns2:BizMsgIdr>\n" +
                "\t\t\t<ns2:MsgDefIdr>CorporateLoan</ns2:MsgDefIdr>\n" +
                "\t\t\t<ns2:BizSvc>UpdateLoanInit</ns2:BizSvc>\n" +
                "\t\t\t<ns2:CreDt>2020-06-25T08:18:33.668Z</ns2:CreDt>\n" +
                "\t\t\t<ns2:CpyDplct>CODU</ns2:CpyDplct>\n" +
                "\t\t\t<ns2:PssblDplct>true</ns2:PssblDplct>\n" +
                "\t\t\t<ns2:Prty/>\n" +
                "\t\t</ns2:AppHdr>\n" +
                "\t\t<Document>\n" +
                "\t\t\t<ns3:MessageId xmlns:ns3=\"http://www.techcombank.com/entity/global/vn/common/datatype/1.0\">ECMLISU.SCF."+momitorID+"."+ackID+"</ns3:MessageId>\n" +
                "\t\t\t<ns4:OriginalRequestReferenceId xmlns:ns4=\"http://www.techcombank.com/entity/global/vn/common/datatype/1.0\">"+momitorID+"</ns4:OriginalRequestReferenceId>\n" +
                "\t\t\t<ns5:AcknowledgementId xmlns:ns5=\"http://www.techcombank.com/entity/global/vn/common/datatype/1.0\">"+ackID+"</ns5:AcknowledgementId>\n" +
                "\t\t\t<ns6:Status xmlns:ns6=\"http://www.techcombank.com/entity/global/vn/common/datatype/1.0\">Accept</ns6:Status>\n" +
                "\t\t\t<ns7:ErrorCode xmlns:ns7=\"http://www.techcombank.com/entity/global/vn/common/datatype/1.0\">000</ns7:ErrorCode>\n" +
                "\t\t\t<ns8:ErrorMessage xmlns:ns8=\"http://www.techcombank.com/entity/global/vn/common/datatype/1.0\">SUCCESS</ns8:ErrorMessage>\n" +
                "\t\t\t<LoanReferenceNumber>"+LD+"</LoanReferenceNumber>\n" +
                "\t\t\t<LoanStatus>Open</LoanStatus>\n" +
                "\t\t\t<LoanValueDate>"+valueDate+"</LoanValueDate>\n" +
                "\t\t\t<LoanMaturityDate>"+maturityDate+"</LoanMaturityDate>\n" +
                "\t\t\t<LoanAmount>"+loanAmount+"</LoanAmount>\n" +
                "\t\t\t<Currency>"+loanCurrency+"</Currency>\n" +
                "\t\t\t<DisbursementInfo>\n" +
                "\t\t\t\t<ns9:DisburseParty xmlns:ns9=\"http://www.techcombank.com/entity/global/vn/loan/corporateloaninfo/1.0\">\n" +
                "\t\t\t\t\t<ns10:PartyId xmlns:ns10=\"http://www.techcombank.com/entity/global/vn/common/datatype/1.0\">"+customerID+"</ns10:PartyId>\n" +
                "\t\t\t\t\t<ns11:PartyAccount xmlns:ns11=\"http://www.techcombank.com/entity/global/vn/common/datatype/1.0\">\n" +
                "\t\t\t\t\t\t<ns11:AccountNumber>"+drawdownAccount+"</ns11:AccountNumber>\n" +
                "\t\t\t\t\t</ns11:PartyAccount>\n" +
                "\t\t\t\t</ns9:DisburseParty>\n" +
                "\t\t\t\t<ns12:DisburseAmount xmlns:ns12=\"http://www.techcombank.com/entity/global/vn/loan/corporateloaninfo/1.0\">"+loanAmount+"</ns12:DisburseAmount>\n" +
                "\t\t\t\t<ns13:DisburseCurrency xmlns:ns13=\"http://www.techcombank.com/entity/global/vn/loan/corporateloaninfo/1.0\">"+loanCurrency+"</ns13:DisburseCurrency>\n" +
                "\t\t\t</DisbursementInfo>\n" +
                "\t\t\t<AllinOneInterestRate>6</AllinOneInterestRate>\n" +
                "\t\t\t<InterestAmount>6</InterestAmount>\n" +
                "\t\t\t<InterestCurrency>VND</InterestCurrency>\n" +
                "\t\t</Document>\n" +
                "\t</LoanInitiateStatusUpdateReq>\n" +
                "</soapenv:Body>\n" +
                "\n" +
                "</soapenv:Envelope>";

        Commons.getLogger().debug("Send request: " + messageRequest);
        SOAPClient soapClient = new SOAPClient();
        try {
            String response = soapClient.requestHTTP(endPointUpdateLoan, messageRequest,"POST", "xml", usernameWS, passwordWS);
            Commons.getLogger().debug("Response call ws: " + response);
            List<String> nodes = Arrays.asList("ns1:Status");
            XmlDriver xmlDriver = new XmlDriver();
            Map<String, String> listNodes = xmlDriver.getNodeValues(response, nodes);
            String responeCode = listNodes.get("ns1:Status");
            if(responeCode.equalsIgnoreCase("0")){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public void main(){
        String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wsac=\"http://temenos.com/WSACHPayments\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <wsac:callOfs>\n" +
                "         <!--Optional:-->\n" +
                "\t\t<OfsRequest>CUSTOMER,IND.TCB/I/PROCESS,SONBN01/Mm123456789/,,MNEMONIC=TEST4564,NAME.1:1=Bui Le Ngoc Mai,GENDER=FEMALE,BIRTH.INCORP.DATE=19910421,NATIONALITY=VN,CITIZEN.ID=173362570,NGAY.CAP.CMT=20090204,NOI.CAP.CMT=HA-NOI,PASSPORT.ID=,TERM.START.DATE=,NARRATIVES:1=,RESIDENCE=VN,PROVINCE.TCB=HA-NOI,TOWN.TCB:1=268,TOWN.TCB:2=09568,INTERESTS:1=To 10,TELEPHONE:2=0949880246,EMAIL:1=sonbnptit@gmail.com,OTHER.NATIONALITY=VN,PROVINCE.2:1=HA-NOI,TOWN.TCB:3=268,TOWN.TCB:4=09568,INTERESTS:3=To 10,DOMICILE=VN,PROVINCE.3.TCB:1=HA-NOI,TOWN.TCB:5=268,TOWN.TCB:6=09568,INTERESTS:5=To 10,TEXT:1=TEST,LEGAL.ID:1=42535234,LEGAL.DOC.NAME:1=MST,LEGAL.ISS.DATE:1=20190415,,SECTOR=4021,INDUSTRY=9013,AHCT=NO,JOB=Cong nghe thong tin,JOB.TITLE:1=1,ACCOUNT.OFFICER=9198,</OfsRequest>\n" +
                "      </wsac:callOfs>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        SOAPClient soapClient = new SOAPClient();
        try {
            String response = soapClient.requestHTTP(endPointUpdateLoan, request,"POST", "xml", usernameWS, passwordWS);
            System.out.println("Response" + response);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


}
