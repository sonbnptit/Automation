#firefox
If $CmdLine[1]="BROWSER_FIREFOX" Then
	ControlFocus("File Upload","","Edit1")
	ControlSetText("File Upload","","Edit1",$CmdLine[2])
	ControlClick("File Upload","","Button1")
#chrome or IE
Else
	ControlFocus("Open","","Edit1")
	ControlSetText("Open","","Edit1",$CmdLine[2])
	ControlClick("Open","","Button1")
EndIf
