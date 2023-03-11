Set Arg = WScript.Arguments
inputFilePath = Arg(0)
remFilePath =  inputFilePath & ".xls"
Set fs = CreateObject("Scripting.FileSystemObject")
fs.MoveFile inputFilePath, remFilePath
Set objExcel = CreateObject("Excel.Application")
Set objWorkbook = objExcel.Workbooks.Open(remFilePath)
objExcel.ActiveWorkbook.SaveAs inputFilePath
objExcel.ActiveWorkbook.Close
fs.DeleteFile remFilePath
