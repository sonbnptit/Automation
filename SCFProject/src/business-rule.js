/*Number là chu so thap phan can lay*/
function funcEval(expression,number){
if(number === undefined)
	return eval(expression).toString();
else
	return eval(expression).toFixed(number).toString();
}
/**
 * Javascript functions processing String
 */
function funcReplace(orgString, oldValue, newValue){
	orgString = orgString.replace(']',"").replace('[',"");
	newValue = newValue || '';
	var regex = new RegExp(oldValue, "g");
	return orgString.replace(regex, newValue);
}
/**
Return index of of subString
**/
function funcIndexOf(orgString, substring){
	return orgString.indexOf(substring);
}
/**
Return last index of of subString
**/
function funcLastIndexOf(orgString, substring){
	return orgString.lastIndexOf(substring);
}

/**
Return length of orgString
**/
function funcLength(orgString){
	return orgString.length;
}

function funcSubstring(orgString, start, end){
	if(!isNullOrEmpty(end))
	return orgString.substring(start, end);
	var indexStart = funcIndexOf(orgString,start);
	return orgString.substring(indexStart, orgString.length);
}
function funcGetColumn(orgString, column){
	return orgString.split(/\s+/)[column];
}
function funcGetRow(orgString, row){
	return orgString.split("\n")[row];
}
function funcContain(orgString, substring){
	return orgString.indexOf(substring) > -1;
}
function funcToLowerCase(orgString){
	return orgString.toLowerCase()
}
function funcToUpperCase(orgString){
	return orgString.toUpperCase()
}

Boolean.parseBoolean = function (val) {
  if (val === 'false') return false;
  return !!val;
};
/**
if operator true return valueTrue
if operator fail return valueFail
**/
function funcTernaryOperator(operator,valueTrue,valueFail){
	var ope = funcEval(operator);    
	var result = Boolean.parseBoolean(ope)?valueTrue:valueFail;
	return result;
	
}
/**
 * Javascript functions processing Number
 */
 function pad(s, len, c){
	s = s + '';
	c = c || '0';
	while(s.length< len) 
		s= c + s;
	return s;
}
function funcToFix(num,c){
  return Number(num).toFixed(c).toString();
}
/**
 * Javascript functions processing Date time
 */
function funcGetTime(){
  var date = new Date();
  var hour = pad(date.getHours(), 2, '0');
  var min = pad(date.getMinutes(), 2, '0');
  var sec = pad(date.getSeconds(), 2, '0');
  var milisec = pad(date.getMilliseconds(), 3, '0');
  return hour + min + sec;
}

/**
	convert ngay tu dung inFormat sang out formatMoney
	date:20180212
	inFormat:yyyyMMdd
	outFormat:YYYY-MM-dd
	return: 2018-02-12
**/
function funcFormatDate(date, inFormat, outFormat){
	var _year = /yyyy/g;
	var _month = /MM/g;
	var _day = /dd/g;
  
	if (match = _year.exec(inFormat)){
		outFormat = outFormat.replace(_year, date.substring(match.index, _year.lastIndex));
	}
  
	if (match = _month.exec(inFormat)){
		outFormat = outFormat.replace(_month, date.substring(match.index, _month.lastIndex));
	}
  
	if (match = _day.exec(inFormat)){
		outFormat = outFormat.replace(_day, date.substring(match.index, _day.lastIndex));
	}
  return outFormat;
}


/**
 * Calculate interest amount
 value = (endDate - start date)*laiSuat/365
 principal: tien goc
 matureRate: lai suat dao han
 bidRate: lai rut truoc han
 today: ngay rut
 validDate: ngay hieu luc
 maturityDate: ngay dao han
 mask: Kieu format cua ngay VD: yyyyMMdd
 */
function funcInterestAmount(principal, ratePerYear, startDate, endDate, mask){
	var date1 = new Date(funcFormatDate(startDate, mask, 'yyyy-MM-dd'));
	var date2 = new Date(funcFormatDate(endDate, mask, 'yyyy-MM-dd'));
	var timeDiff = Math.abs(date2.getTime() - date1.getTime());
	var diffDays = timeDiff / (1000 * 3600 * 24); 
	return Math.round((parseFloat(principal) * (parseFloat(ratePerYear) / 100) * diffDays / 365)).toString();
}


function funcPriodDepositLD(startDate, endDate, mask){
	var date1 = new Date(funcFormatDate(startDate, mask, 'yyyy-MM-dd'));
	var date2 = new Date(funcFormatDate(endDate, mask, 'yyyy-MM-dd'));
	var timeDiff = Math.abs(date2.getTime() - date1.getTime());
	var diffDays = timeDiff / (1000 * 3600 * 24); 
	return diffDays;
}



//Get maturity date by start date and period.Ngay chu nhat hoac sau 17h se len 1 ngay
function funcGetMaturityDate(sDate, period,periodType,inFormat,mask) {

	//get start date;
	var startDate = funcFormatDate(sDate, inFormat, "MM/dd/yyyy");
	startDate = new Date(startDate);	
	var date = funcFormatDate(sDate, inFormat, "MM/dd/yyyy");
	date = new Date(date);	
	
//get duration when period type is day
	if(periodType=='D'){
		date.setDate(date.getDate() + parseInt(period));
	
	}
	//get duration when period type is week
	if(periodType=='W'){
		var duration = parseInt(period)*7;
		date.setDate(date.getDate() + parseInt(duration));
		alert("date W: "+date);
	}
	//get duration when period type is Month
	if(periodType=='M'){		
		date.setMonth(date.getMonth() + parseInt(period));
	
	}
//get duration when period type is year
	if(periodType=='Y'){
		var duration = parseInt(period)*12;
		date.setMonth(date.getMonth() + parseInt(period));
		
	}
	
		var currentDate = new Date();
		var hour = pad(currentDate.getHours(), 2, '0');	        
		if (startDate.getDay() == 0||hour>=17) {
			date.setTime(date.getTime() + (24 * 60 * 60 * 1000));			
		}
		var dd = date.getDate();
		var mm = date.getMonth() + 1; //January is 0!
		var yyyy = date.getFullYear();
		
		if (dd < 10) {
			dd = '0' + dd;
		}
		if (mm < 10) {
			mm = '0' + mm;
		}
		var result = yyyy+mm+dd;
	return funcFormatDate(result,'yyyyMMdd',mask);
	}
	//Get maturity date by start date and period.Ngay chu nhat hoac sau 17h se len 1 ngay
function funcGetValueDate(sDate,inFormat,mask) {

	//get start date;
	var startDate = funcFormatDate(sDate, inFormat, "MM/dd/yyyy");
	startDate = new Date(startDate);	
	
		var currentDate = new Date();
		var hour = pad(currentDate.getHours(), 2, '0');	        
		if (startDate.getDay() == 0||hour>=17) {
			startDate.setTime(startDate.getTime() + (24 * 60 * 60 * 1000));		
		}
		var dd = startDate.getDate();
		var mm = startDate.getMonth() + 1; //January is 0!
		var yyyy = startDate.getFullYear();
		
		if (dd < 10) {
			dd = '0' + dd;
		}
		if (mm < 10) {
			mm = '0' + mm;
		}
		var result = yyyy+mm+dd;
	return funcFormatDate(result,'yyyyMMdd',mask);
	}
/**
remove moneyType and , in money
example: working balance is VND 1,000,000,000
return: 1000000000
**/
function funcGetMoney(type,amount){
  amount = amount.replace(']','').replace('[','');	
  amount = amount.replace(type,"").trim();  
  amount = amount.replace(new RegExp("&com", 'g'), "");
  return amount;
}
/**
remove moneyType and , in money
Number fix: total number decimal
example: working balance is VND 1,000,000,000;1,000.8
return: 1000000000.00;1000.80
**/
function funcFormatMoney(type,amount,numberFix){
  amount = amount.replace(']','').replace('[','');	
  amount = amount.replace(type,"").trim();
  var lstMoney = amount.split(";");
  var finalListAmount="";
 for(var i=0;i<lstMoney.length;i++){
	var tmp = lstMoney[i].replace(new RegExp("&com", 'g'), "");
	if(i==0)
		finalListAmount = funcToFix(tmp,numberFix);  
	else
		finalListAmount = finalListAmount+";"+funcToFix(tmp,numberFix);  
 }
  return finalListAmount;
}


/**
	Ham lay gia tri lai suat den thoi diem hien tai
**/
function funcCRAmount(crAmount){
	crAmount=crAmount.replace(']',"").replace('[',"");
	var lstCRAmount = crAmount.split(";");
	var CRAmountValue = 0;
	for(var i=0;i<lstCRAmount.length;i++){	
		if(funcContain(lstCRAmount[i],'null')){
			lstCRAmount[i] = "0";
		}
		 CRAmountValue = CRAmountValue + parseInt(lstCRAmount[i]);
	}
	return funcToFix(CRAmountValue,0);
}
/**
 return description for account type
**/
function funcGetAccountType(category){
	if(category==1252||category==21051||category==21052||category==21053||category==21054||category==21055||category==21056||category==21061||category==21003||category==2001||category==1411)
		return 'Loan payment';
	else if(category==3005||category==3040||category==3050||category==3025||category==3032||category==21004||category==21006||category==21005)
		return 'Term deposit';	
	else if(category==1001||category==1004||category==1012||category==1020||category==1021||category==1023||category==1306||category==3006)
		return 'Current account';
	else if(category==1002||category==1005||category==1009||category==1010||category==1015||category==1016||category==1024||category==1025||category==1031||category==1032||category==1050||category==1061||category==1062||category==1063||category==1064||category==1065||category==1066||category==1067||category==1068||category==1069||category==1070)	
		return 'Capital account';
	else if(category==1008||category==1018)
		return 'Overdraft account';
	else if(category==3021)
		return 'Savings account';
}


/*
Return string radom
*/
function funcRandomString(len, charSet) {
    charSet = charSet || 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz';
    var randomString = '';
    for (var i = 0; i < len; i++) {
        var randomPoz = Math.floor(Math.random() * charSet.length);
        randomString += charSet.substring(randomPoz,randomPoz+1);
    }
    return randomString;
}

/**
Lay danh sach ID STMT NO (ID trong bang STMT.ENTRY) tu giá trị smtmnos tra ve tu bang fund transfer
**/
function funcGetListSTMTNo(stmtNos){
	stmtNos = stmtNos.replace(']','').replace('[','');	
	var listSTMT="";
	stmtNos = funcReplace(stmtNos," ",";");	
	listSTMT = stmtNos.split(';');
	var arrSTMT="";
	var k =0;
	var test="";
	for(var i = 0; i<listSTMT.length;i++){    
		if(funcContain(listSTMT[i],'.')){
			var listSTMTTmp = listSTMT[i].replace('"','');
			do{
				i=i+1;	
			}
			while(listSTMT[i]=='');
			var numberSTMT = listSTMT[i].split('-');		
			
			
			for(var j=parseInt(numberSTMT[0]);j<=parseInt(numberSTMT[1]);j++){				
				if(k==0)arrSTMT = listSTMTTmp+'000'+j;
				else
				arrSTMT = arrSTMT+";"+(listSTMTTmp+'000'+j);       
				k++;				
			}				
		}			
	}
	return arrSTMT;
}
/**
Lay ra ID tu list các ID theo so thu tu
**/
function funcGetSTMTNO(listSTMT, index,type){
if(type === undefined){
	listSTMT = listSTMT.replace(']','').replace('[','');	
	 var arrayList = listSTMT.split(';');	
	 	return arrayList[index];
	}else{	
	listSTMT = listSTMT.replace(']','').replace('[','');	
	 arrayList = listSTMT.split(type);	
	 	return arrayList[index];
	}
	return '';
}
/**
Lay so luong doi tuong cach nhau bang dau ;
**/
function funcGetSize(listSTMT,type){
	if(funcIsNullOrEmpty(listSTMT)) return 0;
	if(type === undefined){
	listSTMT = listSTMT.replace(']','').replace('[','').replace('<<null>>','');	
	var arrayList = listSTMT.split(';');
	return arrayList.length;
}else{

	listSTMT = listSTMT.replace(']','').replace('[','').replace('<<null>>','');	
	var arrayList = listSTMT.split('type');
	return arrayList.length;
}
}


/**
	get interest rate in T24
**/
function funcGetT24Interest(paramPeriod, t24_period,t24_bidRate){
	t24_period = t24_period.replace(']',"").replace('[',"");
	t24_bidRate = t24_bidRate.replace(']',"").replace('[',"");
	var listT24_period = t24_period.split(";");
	var listT24_bidRate = t24_bidRate.split(";");
	for(var i=0;i<listT24_period.length;i++){	
		if(listT24_period[i]==paramPeriod)
			return listT24_bidRate[i];
	}
}
/**
	get maturityDate in T24
**/
function funcGetT24MaturityDate(paramPeriod, t24_period,t24_restDate){
	t24_period = t24_period.replace(']',"").replace('[',"");
	t24_restDate = t24_restDate.replace(']',"").replace('[',"");
	var listT24_period = t24_period.split(";");
	var listT24_restDate = t24_restDate.split(";");
	for(var i=0;i<listT24_period.length;i++){	
		if(listT24_period[i]==paramPeriod)
			return listT24_restDate[i];
	}
}
/**
convert period
**/
function funcConvertPeriod(period){
	if(!funcContain(period,"D")&&!funcContain(period,"W"))
		period = period+"M";
	return period;
}
/**
	Get Interest
**/
function funcInterestByInsurance(Insurance, interest){
	if(Insurance=='Y')
		return interest;
	if(Insurance=='N')
	        return (Number(interest)+0.2).toFixed(2).toString();	
}
/**
Get list amount TKDK deposit
**/
function funcGetListRepayAmount(pricipal, interestRate,T24kDate){
	var repayAmount="";
	T24kDate = T24kDate.replace(']',"").replace('[',"");
	var listDate = T24kDate.split(";");
	startDate = listDate[0];
	for(var i=0;i<listDate.length;i++){	
	  
	 var repay = funcInterestAmount(pricipal, interestRate, startDate, listDate[i], 'yyyyMMdd');
	 if(i==0){
		 repayAmount = repay;
		 continue;
	 }
	 if(i == listDate.length-1)	
		repay = parseFloat(repay) + parseFloat(pricipal);	
		repayAmount = repayAmount +";"+repay;
	 startDate = listDate[i];
	}
	return repayAmount;
}
/**
	get locator via xpath
**/
function getElementByXpath(path) {
  return document.evaluate(path, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;
}


/**
mask is YYYY-MM-dd/ DD-MM-YYYY....
**/
function funcGetToday(mask){
	var date = new Date();
  var year = pad(date.getFullYear(), 4, '0');
  var moth = pad(date.getMonth()+1, 2, '0');
  var date = pad(date.getDate(), 2, '0');
  var today = year+moth+date;
  return funcFormatDate(today, 'yyyyMMdd', mask);  
}
/**
	Add number day to date
**/
 function funcAddDatetoDay(dateAdd, days, mask){
    var result = new Date(dateAdd);
	result.setDate(result.getDate() + parseInt(days));
	var year = pad(result.getFullYear(), 4, '0');
	var moth = pad(result.getMonth()+1, 2, '0');
	
	var date = pad(result.getDate(), 2, '0');
	var result1 = year+moth+date;
	
	return funcFormatDate(result1,'yyyyMMdd',mask)
}

/**
	Add number month to date
**/
 function funcSubMonthToDate(dateAdd, months, mask){
    var result = new Date(dateAdd);
	result.setMonth(result.getMonth() - parseFloat(months));
	var year = pad(result.getFullYear(), 4, '0');
	var moth = pad(result.getMonth()+1, 2, '0');
	
	var date = pad(result.getDate(), 2, '0');
	var result1 = year+moth+date;
	return funcFormatDate(result1,'yyyyMMdd',mask)
}
/**
	return day from today before numberDay
**/
 function funcGetDateBeforeDays(numberdays, mask){
   var result = new Date();
	result.setDate(result.getDate() - parseInt(numberdays));
	var year = pad(result.getFullYear(), 4, '0');
	var moth = pad(result.getMonth()+1, 2, '0');
	
	var date = pad(result.getDate(), 2, '0');
	var result1 = year+moth+date;
	
	return funcFormatDate(result1,'yyyyMMdd',mask)
}

/**
	return day from today before numberMonths
**/
 function funcGetDateBeforeMonths(numberMonths, mask){
    var result = new Date();
	result.setMonth(result.getMonth() - parseFloat(numberMonths));
	var year = pad(result.getFullYear(), 4, '0');
	var moth = pad(result.getMonth()+1, 2, '0');
	
	var date = pad(result.getDate(), 2, '0');
	var result1 = year+moth+date;
	return funcFormatDate(result1,'yyyyMMdd',mask)
}
/**
list account
**/

function funcConvertListChargeAccount(AccountNo,AccountTile){
	AccountNo = AccountNo.replace(']',"").replace('[',"");
	AccountTile = AccountTile.replace(']',"").replace('[',"");
	var listAccountNo = AccountNo.split(";");
	var listAccountTitle = AccountTile.split(";");
	var listDisplay="";
	for(var i=0;i<listAccountNo.length;i++){	
		listDisplay = listDisplay+listAccountTitle[i]+" "+listAccountNo[i]+";";
	}
	return listDisplay;
}

/**
get day
**/

function funcgetDay(getDate){
	var getDate1 = getDate.split("/");
	var getDay=getDate1[0]; 
	return getDay;
}

/**
check null or blank
**/
function isNull(value){
	return value === undefined || value === null;
}

function isNullOrEmpty(value){
	return value === undefined || value == null || value.length === 0;
}

function funcIsNullOrEmpty(value,nullIdicator){
	if(isNullOrEmpty(nullIdicator)) nullIdicator = '<<null>>';
	return value === undefined || value == null || value.length === 0 || value == nullIdicator;
}

/**
get Item Index
**/
function getItemIndex(item, lstItem) {
  for (var i = 0; i < lstItem.length; i++) {
    if (lstItem[i] === item) return i;
  }
  return -1;
}

function getContainItemIndex(item, lstItem) {
  for (var i = 0; i < lstItem.length; i++) {
    if (lstItem[i].indexOf(item) !== -1) return i;
  }
  return -1;
}

function funcGetItemIndex(item, list, separator) {
  if(isNullOrEmpty(separator))
	  separator = ";";
  var lstItem = list.split(separator);
  return funcToFix(getItemIndex(item, lstItem),0);
}

function funcGetContainItemIndex(item, list, separator) {
  if(isNullOrEmpty(separator))
	  separator = ';';
  var lstItem = list.split(separator);
  return funcToFix(getContainItemIndex(item, lstItem),0);
}

function funcGetListDistinct(list, separator) {
  if(isNullOrEmpty(separator))
	  separator = ";";
  var lstItem = list.split(separator);
  var listResult = [];
  for(var i=0;i<lstItem.length;i++){
	  if(getItemIndex(lstItem[i],listResult)===-1)
		  listResult.push(lstItem[i]);
  }
  return listResult;
}

function funcExtractRegex(strVal, regexStr, group){
  strVal = strVal.replace('&lparen','(').replace('&rparen',')').replace('&com',':');
  regexStr = regexStr.replace('&lparen','(').replace('&rparen',')').replace('&com',':');
  var regex = new RegExp(regexStr, "g");
  var match;
  var resulstLst = '';
  while (match = regex.exec(strVal)) {
	if(group >= match.length) continue;
	if(resulstLst.length > 0) resulstLst += ";";
	resulstLst += match[group];
  }
  return resulstLst;
}

/*
Get a new matrix are belongs to matrix2 and not in matrix1
*/
function funcMatrixContainRow(matrix, row, sepRow, sepCol, dateFormat){
	if(funcIsNullOrEmpty(sepRow)) sepRow = ";";
	if(funcIsNullOrEmpty(sepCol)) sepCol = ",";
	if(funcIsNullOrEmpty(row)) return true;
	if(funcIsNullOrEmpty(matrix)) return false;

	//matrix = matrix.replace('"[','').replace(']"','').replace('[','').replace(']','').replace(new RegExp("\\s*&com\\s*", 'g'), "&com");
	row = row.replace(new RegExp("\\s*&com\\s*", 'g'), sepCol);

	//get row list in matrix
	var rowList = matrix.split(sepRow);
	//get col list in row
	var rowCol = row.split(sepCol);

	for(var i=0;i<rowList.length;i++){
		//get col list in a row of matrix
		var matrixRow = rowList[i].replace(new RegExp("\\s*&com\\s*", 'g'), ",");
		var matrixCol = matrixRow.split(sepCol);
		if(matrixCol.length !== rowCol.length) continue;	//length not equal

		var j=0;
		for(j=0;j<matrixCol.length;j++){
			if(isNullOrEmpty(matrixCol[j]) && isNullOrEmpty(rowCol[j])) continue;
			if(isNaN(matrixCol[j])) {	//not number => comparision date or string
				if(matrixCol[j].trim() !== rowCol[j].trim()){ // => comparision string
					if(!isNullOrEmpty(rowCol[j].trim().match("[0-9]+-[0-9]+-[0-9]+")) && !isNullOrEmpty(getFormatDate(matrixCol[j].trim(), dateFormat))){	// => comparision date
						alert(getFormatDate(matrixCol[j].trim(), dateFormat));
						alert(getFormatDate(rowCol[j].trim(), dateFormat));
						if(getFormatDate(matrixCol[j].trim(), dateFormat) !== getFormatDate(rowCol[j].trim(), dateFormat)) break;
					}else break;
				}
			}
			//if(isNaN(matrixCol[j]) && matrixCol[j].trim() !== rowCol[j].trim()) break;	//if not number => comparision string
			if(isNaN(matrixCol[j]) !== isNaN(rowCol[j])) break;	// one number, other not number => not equal
			if(!isNaN(matrixCol[j]) && parseFloat(matrixCol[j]) !== parseFloat(rowCol[j])) break; //if number => comparision number
		}
		if(j === matrixCol.length) return true;	//matrix contain all col of row
	}
	return false; //matrix not contain row
}
function funcGetDiffMatrix(matrix1, matrix2, sepRow, sepCol, dateFormat){
	if(funcIsNullOrEmpty(sepRow)) sepRow = ";";
	if(funcIsNullOrEmpty(sepCol)) sepCol = ",";
	if(funcIsNullOrEmpty(dateFormat)) dateFormat = "";
	if(funcIsNullOrEmpty(matrix1)) return matrix2;
	if(funcIsNullOrEmpty(matrix2)) return "";


	matrix1 = matrix1.replace('"[','').replace(']"','').replace('[','').replace(']','').replace(new RegExp("\\s*&com\\s*", 'g'), "&com");
	matrix2 = matrix2.replace('"[','').replace(']"','').replace('[','').replace(']','').replace(new RegExp("\\s*&com\\s*", 'g'), "&com");

	var rowList1 = matrix1.split(sepRow);
	var rowList2 = matrix2.split(sepRow);
	var diffRows = [];
	for(var i=0;i<rowList2.length;i++){
		var rowItem = rowList2[i];
		if(funcMatrixContainRow(matrix1,rowItem, sepRow, sepCol, dateFormat)===false){
			diffRows.push(rowItem);
		}
	}
	return diffRows.join(sepRow);

}

function funcMatrixEqual(matrix1, matrix2, sepRow, sepCol, dateFormat){
	if(funcIsNullOrEmpty(sepRow)) sepRow = ";";
	if(funcIsNullOrEmpty(sepCol)) sepCol = ",";
	if(funcIsNullOrEmpty(matrix1) && funcIsNullOrEmpty(matrix2)) return true;	//both matrix null => true
	if(funcIsNullOrEmpty(matrix2) || funcIsNullOrEmpty(matrix2)) return false;	//one of mtrix null => false

	matrix1 = matrix1.replace('"[','').replace(']"','').replace('[','').replace(']','').replace(new RegExp("\\s*&com\\s*", 'g'), "&com");
	matrix2 = matrix2.replace('"[','').replace(']"','').replace('[','').replace(']','').replace(new RegExp("\\s*&com\\s*", 'g'), "&com");

	var rowList1 = matrix1.split(sepRow);
	var rowList2 = matrix2.split(sepRow);
	for(var i=0;i<rowList2.length;i++){
		var rowItem = rowList2[i];
		if(funcMatrixContainRow(matrix1,rowItem, sepRow, sepCol, dateFormat)===false){
			return false;
		}
	}
	return true;
}

function funcGetMatrixRowcount(matrix, sepRow){
	if(funcIsNullOrEmpty(sepRow)) sepRow = ";";
	if(funcIsNullOrEmpty(matrix)) return 0;
	matrix = matrix.replace('"[','').replace(']"','').replace('[','').replace(']','');
	
	var regExp = new RegExp(sepRow, "gi");
	return (matrix.match(regExp) || []).length + 1;
}

/* END Matrix functions */

/*
Return max value
*/
function funcGetMaxValue(value){
	value = value.replace('"[','').replace(']"','');
	var listValue = value.split(';');
	if(value==''||value=='<<null>>') return 0;
	var maxValue=Number(listValue[0]);
	for(i=1;i<listValue.length;i++){		
		if(maxValue < Number(listValue[i])){
			maxValue = Number(listValue[i]);
		}
	}
	return maxValue;
}
/**
periodValue: Current Day,Current Month,Previous Day,Previous Month,Last 1 Week,Last 2 Weeks,Last 5 Days
return Fromdate via currentDate
**/
function funcGetFromDate(periodValue,mask){
	var toDate = new Date();
	var period=0;
	if(periodValue=='Current Day')
		period = 0;
	else if(periodValue=='Last 1 Week')
		period = -7;
	else if(periodValue=='Last 2 Weeks')
		period = -14;
	else if(periodValue=='Last 5 Days')
		period = -5;
	else if(periodValue=='Current Month'){
		 var date = pad(toDate.getDate(), 2, '0')-1;		 
		period = -date;
	}else if(periodValue=='Previous Month'){
		 var date = pad(toDate.getDate(), 2, '0');	
		 toDate.setDate(toDate.getDate() + parseInt(-date));		 
		date =  pad(toDate.getDate(), 2, '0')-1;	
		period = -date;
	}
	var fromDate = funcAddDatetoDay(toDate,period,mask);
	return fromDate;
}

/**
	Return toDate
**/
function funcGetToDate(periodValue,mask){
	var toDate = new Date();
	var period = 0;
	if(periodValue=='Previous Month'){
		period= -pad(toDate.getDate(), 2, '0');	
	}	 
	 var endDate = funcAddDatetoDay(toDate,period,mask);
	 return endDate;
	
}

/**
remove moneyType and , in money
Number fix: total number decimal
example: working balance is 22-03-2018;22-12-2019
return: 20180322;20191222
**/
function funcGetListFormatDate(listDate,inFormat,outFormat){
  listDate = listDate.replace(']','').replace('[','');	  
  var dates = listDate.split(";");
  var finalListDate="";
  
 for(var i=0;i<dates.length;i++){	
	if(i==0)
		finalListDate = funcFormatDate(dates[i],inFormat,outFormat);  
	else
		finalListDate = finalListDate+";"+funcFormatDate(dates[i],inFormat,outFormat);  
 }
  return finalListDate;
}


/**
Convert number by dicimal: (6 or 7. or 8.0 or 9.00 or 10.0000) to 6.00 or 7.00 or 8.00 or 9.00 or 10.00
**/
function funcConvertMoney(money,dicimal){
	var finalMoney = money;
	var tmpMoneys = money.split(".");

	if (tmpMoneys.length > 1) {
		var decimalStr='';
		var decimalMoney = tmpMoneys[1];
		if (decimalMoney.length > dicimal) {
			decimalStr = decimalMoney.substring(0, dicimal);
		} else if (decimalMoney.length < dicimal) {
			decimalStr = decimalMoney;
			for (var i = 0; i < dicimal - decimalMoney.length; i++) {
				decimalStr += "0";
			}
		} else {
			decimalStr = decimalMoney;
		}
		finalMoney = tmpMoneys[0] + "." + decimalStr;
	} else {
		for (var i = 0; i < dicimal; i++) {
			if (i == 0) {
				finalMoney += ".";
			}
			finalMoney += "0";
		}
	}
	
	return finalMoney;
}
/**
return list money contains numberFix dicimal no round
**/
function funcGetMoneyNoRound(moneys,numberFix){
	moneys = moneys.replace(']','').replace('[','');	  
  var lstMoney = moneys.split(";");
  var finalListAmount="";
 for(var i=0;i<lstMoney.length;i++){
	var tmp = lstMoney[i].replace(new RegExp("&com", 'g'), "");
	if(i==0)
		finalListAmount = funcConvertMoney(tmp,numberFix);  
	else
		finalListAmount = finalListAmount+";"+funcConvertMoney(tmp,numberFix); 
 }
  return finalListAmount;
}
/**
return value[index]
Key: example: ; or , or any letter
**/
function funcGetValueByIndex(listValue,index,key){
	//return listValue+" key"+key;
	listValue = listValue.replace(']','').replace('[','');	
	var arrayList = listValue.split(key);	
	return arrayList[index];
}
/* Format function */
/* funcFormatList("abcd;efgh", ";", ",", "'", "'")
/* abcd;efgh => 'abcd','efgh' */
function funcFormatList(list, sepList, sepFormat, preItem, sufItem){
	preItem = preItem === undefined ? '' : preItem;
	sufItem = sufItem === undefined ? '' : sufItem;
	list = list.replace('[','').replace(']','');
	var listValue = list.split(sepList);
	var formatList = [];
	for(i=0;i<listValue.length;i++){
		formatList.push(preItem + String(listValue[i]) + sufItem);
	}
	return formatList.join(sepFormat);
}

/* param1: textValues --> 9.93 abc;111abc;...
/* param2: unit --> xyz
/* param3: separatorIn --> ; or | or ...
/* param4: separatorOut --> ; or | or ...
/* out:	   9.93xyz;111xyz;...
*/
function funcConvertTextToNumber(textValues, unit, separatorIn, separatorOut) {
	var results = '';
    if (separatorIn == '') {
		separatorIn = ";";
	}
	if (separatorOut == '') {
		separatorOut = ";";
	}
	textValues = textValues.replace(']','').replace('[','');
	var arrayValue = textValues.split(separatorIn);
	for(var i = 0; i < arrayValue.length; i++){
		if (i == 0) {
			results = parseFloat(arrayValue[i]) + unit;
		} else {
			results += separatorOut + parseFloat(arrayValue[i]) + unit;
		}
	}
	return results;
}

function getFormatDate(dateString, dateFormat){
	var dt = new Date(dateString);
	if(dt === null || !dt) return "";
	return dateFormat.replace("yyyy", dt.getFullYear()).replace("MM", dt.getMonth() + 1).replace("dd", dt.getDate())
		.replace("HH", dt.getHours()).replace("mm", dt.getMinutes()).replace("ss", dt.getSeconds());
}

function getDiffMonthsTwoDates(dt2, dt1) {
  var diff =(dt2.getTime() - dt1.getTime()) / 1000;
   diff /= (60 * 60 * 24 * 7 * 4);
  return Math.abs(Math.round(diff));
  
 }
 
 /**
 Convert Months --> M, Days -->D, Year -> Y
 **/
 function funcConvertTenor(listTenor){
  listTenor = listTenor.replace(']','').replace('[','');	  
  var tenors = listTenor.split(";");
  var finalListTenor="";
  
 for(var i=0;i<tenors.length;i++){	
	var tenor= tenors[i].split(" ");
	if(i==0)
		finalListTenor = tenor[0]+  funcSubstring(tenor[1],0,1);
	else
		finalListTenor = finalListTenor+";"+tenor[0]+  funcSubstring(tenor[1],0,1);
 }
  return finalListTenor;
}


/**
Change 1 --> Jan, 2 --> Feb....
**/
function funcConvertMonth(numberMonth){

if(numberMonth=='1' ||numberMonth=='01' ) 	
		return 'Jan';		
if(numberMonth=='2' ||numberMonth=='02' ) 
		return 'Feb';
if(numberMonth=='3' ||numberMonth=='03' ) 
		return 'Mar';	
if(numberMonth=='4' ||numberMonth=='04' ) 
		return 'Apr';
if(numberMonth=='5' ||numberMonth=='05' ) 
		return 'May';
if(numberMonth=='6' ||numberMonth=='06' ) 
		return 'Jun';
if(numberMonth=='7' ||numberMonth=='07' ) 
		return 'Jul';
if(numberMonth=='8' ||numberMonth=='08' ) 
		return 'Aug';
if(numberMonth=='9' ||numberMonth=='09' ) 
		return 'Sep';
if(numberMonth=='10' ) 	
		return 'Oct';
if(numberMonth=='11' )
		return 'Nov';
if(numberMonth=='12' )
		return 'Dec';					

return '';

}

/**
dd <10 --> only display number not 02
**/
function funcConvertDay(numberDay){
 if(numberDay<10)
	return funcReplace(numberDay,'0','');
	return numberDay;
}

function funcGetMonthShortName(monthNo){
	var monthShortNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
	return monthShortNames[Number(monthNo) - 1];
}
 /*
   Create new line in the strings
 */
function funcNewLineStrings(listStrings) {
	listStrings = listStrings.replace(']','').replace('[','');	  
    var Strings = listStrings.split(";");
	
	for(var i=0;i<Strings.length;i++){	
	   if(i==0)
		  goToList = Strings[i];  
	   else
		   goToList += Strings[i] + "\n";
	}
	return goToList;
}

/**
get xpath from ldata
**/
 function funcGetXpath(listInputFile){
 var xpath;
 	listInputFile = listInputFile.replace(']','').replace('[','');	  
    var inputFiles = listInputFile.split(";");
	for(var i=0;i<inputFiles.length;i++){	
	   if(i==0)
		  xpath = "contains(@id,'"+inputFiles[i]+"')";  
	   else
		   xpath += "or contains(@id,'"+inputFiles[i]+"')";
	}
	return xpath;
 }
 
 /*Tính giá trị pass/fail trong mỗi event attribute*/
 
 function funcConverOperator(rule_operator,staticValue,tagetValue){
     
	if(rule_operator=='eq') return ((tagetValue=='<<null>>')||(staticValue==tagetValue));
	else if(rule_operator=='nin') return ((tagetValue=='<<null>>')||(!funcContain(staticValue,tagetValue)));
	else if(rule_operator=='lt') return ((tagetValue=='<<null>>')||(tagetValue<staticValue));
	else if(rule_operator=='le') return ((tagetValue=='<<null>>')||(tagetValue<=staticValue));
	else if(rule_operator=='gt') return ((tagetValue=='<<null>>')||(tagetValue>staticValue));
	else if(rule_operator=='ge') return ((tagetValue=='<<null>>')||(tagetValue>=staticValue));
 }
 
 
 /*
	Kiem tra charge code có thỏa mãn các event attribute
 */
function funcIsChargeCode(SOURCE_ATTRIBUTE_ID,rule_operator, target_static_value,BusinessProduct,CCY,CurrentTime,Amount){
 	SOURCE_ATTRIBUTE_ID = SOURCE_ATTRIBUTE_ID.replace(']','').replace('[','');	
	rule_operator = rule_operator.replace(']','').replace('[','');	
	target_static_value = target_static_value.replace(']','').replace('[','');	
	var lstSource = SOURCE_ATTRIBUTE_ID.split(";");
	var lstOperator= rule_operator.split(";");
	var lstValue = target_static_value.split(";");
	var result = true;
	for(var i =0;i<lstSource.length;i++){
		if(lstSource[i]=='BUSS_PROD'){
			result = result&&funcConverOperator(lstOperator[i],lstValue[i],BusinessProduct);
		
		}
		else if(lstSource[i]=='PMT_CCY'){
			result = result&&funcConverOperator(lstOperator[i],lstValue[i],CCY);
		}
		else if(!isNullOrEmpty(CurrentTime)&lstSource[i]=='TXN_TIME'){
			result = result&&funcConverOperator(lstOperator[i],lstValue[i],CurrentTime);
		}
		else if(!isNullOrEmpty(Amount)&lstSource[i]=='PMT_AMOUNT'){
			result = result&&funcConverOperator(lstOperator[i],lstValue[i],Amount);
		}
	}
	return result;
 }
 
/**
* xpathHeader: xpath of header row -> //span[contains(@id,'gridcolumn-') and contains(@id,'-textEl')]
* xpathDatas: xpath of datas view -> //tr[contains(@id,'gridview-') and contains(@id,'-record-ext-record-')]
* listColumn: list column result -> the column name is same header name in the browser (PSH File ID|Customer Id|File Name|Value Date|Status)
* conditions: list condition with format input -> column name:=value expected|.... (Status:=Pending Authorization|Customer Id:=32531004)
* return: list value with format value1<|>value2<|>value3<|>...
*/
function funcGetDataByHeader (xpathHeader, xpathDatas, listColumn, conditions) {
	var xpathResType = XPathResult.ORDERED_NODE_SNAPSHOT_TYPE;
	var headings = document.evaluate(xpathHeader, document, null, xpathResType, null);
	var bodyValue = document.evaluate(xpathDatas, document, null, xpathResType, null);

	var results = "";
	if (headings.snapshotLength == 0 || bodyValue.snapshotLength == 0) {
		return results;
	}

	var mapHeader = new Map();
	for (var i = 0; i < headings.snapshotLength; i++) {
		mapHeader.set(headings.snapshotItem(i).textContent.trim(), i);
	}

	var mapConditions = new Map();
	if (!isObjEmpty(conditions)) {
		var arrayCond = conditions.split("|");
		for (var i = 0; i < arrayCond.length; i++) {
			var colConds = arrayCond[i].split(":=");
			var indexColumn = mapHeader.get(colConds[0].trim());
			mapConditions.set(indexColumn, colConds[1]);
		}
	}

	for (var i = 0; i < bodyValue.snapshotLength; i++) {
		var trValue = bodyValue.snapshotItem(i);
		var listTD = trValue.childNodes;
		var isOk = true;
		mapConditions.forEach(function(value, key) {
			if (listTD[key].textContent.trim() != mapConditions.get(key)) {
				isOk = false;
			}
		});

		if (isOk) {
			if (results.length != 0) {
				results += "<===>"; // List Row
			}

			if (!isObjEmpty(listColumn)) { 
				// return values by list header name
				var arrayColumn = listColumn.split("|");
				for (var j = 0; j < arrayColumn.length; j++) {
					var indexCol = mapHeader.get(arrayColumn[j]);
					results += (j == 0) ? listTD[indexCol].textContent: "<|>" + listTD[indexCol].textContent;
				}
			} else {
				// return all value
				mapHeader.forEach(function(value, key) {
					results += (value == 0) ? listTD[value].textContent: "<|>" + listTD[value].textContent;
				});
			}
		}
	}
	//console.log("results: " + results);
	return results;
}

function isObjEmpty (objIn) {
	if (typeof objIn === undefined || typeof objIn === 'undefined' || objIn === "") {
		return true;
	} else {
		return false;
	}
}

function funcGetTextByConditions (objIn, startCond, endCond) {
	var results = "";
	if (typeof endCond === undefined || typeof endCond === 'undefined' || endCond === "") {	
		results = objIn.substring(objIn.lastIndexOf(startCond));
	} else {
		results = objIn.substring(objIn.lastIndexOf(startCond), objIn.lastIndexOf(endCond));
	}
	return results;
}
/**
return value object via key
**/

/**
 Get doi tuong tu list object
 **/
 function funcGetValueByKey(dataTemp,keyCondition,valueCondition,keyTaget){
   dataTemp = dataTemp.replace(new RegExp("&com", 'g'), ",");    
	var data = JSON.parse(dataTemp);
	for(var i =0; i<data.length ;i++){
		if(data[i][keyCondition]==valueCondition)
			return data[i][keyTaget];
	}
 } 