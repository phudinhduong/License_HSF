@echo off
set JLINK_VM_OPTIONS=
set DIR=%~dp0
"%DIR%\java" %JLINK_VM_OPTIONS% -m hsf302.he180446.duonghd.democlient/hsf302.he180446.duonghd.democlient.controller.MainApp %*
