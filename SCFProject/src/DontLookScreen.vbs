Set Excel = WScript.CreateObject("Excel.Application")


Set WshShell = CreateObject("WScript.Shell") 



strComputer = "."
Set objWMIService = GetObject("winmgmts:\\" & strComputer & "\root\cimv2")
Set colItems = objWMIService.ExecQuery("Select * from Win32_DesktopMonitor",,48)

For Each objItem in colItems
	maxY = objItem.ScreenHeight
	maxX = objItem.ScreenWidth
Next

min=1
counter = 0

Type POINTAPI
	X_Pos As Long
	Y_Pos As Long
End Type
Dim Hold As POINTAPI

'Do 
Do While True
	WScript.Sleep (30000)
	'counter = counter + 1
	
	Randomize
	x = (Int((maxX-min+1)*Rnd+min)) & ""
	Randomize
	y = (Int((maxY-min+1)*Rnd+min)) & ""
	
'WScript.Echo "maxX " & x & "Y "  &  y 
	Excel.ExecuteExcel4Macro ( _
	"CALL(""user32"",""SetCursorPos"",""JJJ""," & x & "," & y & ")")
	
	'WScript.Sleep (4000)
	
Loop	
'Loop Until counter = 10


WScript.Echo "Program Ended" & counter
    
    
    'To Kill mouse move
    'Dim WshShell
    'Set WshShell = WScript.CreateObject("WScript.Shell")
    'WshShell.Run "taskkill /f /im Cscript.exe", , True 
    'WshShell.Run "taskkill /f /im wscript.exe", , True  