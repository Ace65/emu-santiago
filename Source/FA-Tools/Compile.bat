@echo off
title Aion Fantasy  Emulator BuildAll Console

CLS
:MENU
ECHO.
ECHO.
ECHO.
ECHO                                  ''~``
ECHO                                 ( o o )
ECHO    ------------------------.oooO--(_)--Oooo.------------------------
ECHO    .             1 - Build FA-GameServer server                       .
ECHO    .             2 - Build FA-LoginServer server                      .
ECHO    .             3 - Build FA-ChatServer server                       .
ECHO    .             4 - Quit                                          .
ECHO    .                         .oooO                                 .
ECHO    .                         (   )   Oooo.                         .
ECHO    ---------------------------\ (----(   )--------------------------
ECHO                                \_)    ) /
ECHO                                      (_/
ECHO.
ECHO.

SET /P Ares=Type 1, 2 ,3, or 4 to QUIT, then press ENTER:

IF %Ares%==1 GOTO GameServer
IF %Ares%==2 GOTO LoginServer
IF %Ares%==3 GOTO ChatServer
IF %Ares%==4 GOTO QUIT

:FULL

cd ..\FA-GameServer
start /WAIT /B ..\FA-Tools\Ant\bin\ant clean dist

cd ..\FA-LoginServer
start /WAIT /B ..\FA-Tools\Ant\bin\ant clean dist

cd ..\FA-ChatServer
start /WAIT /B ..\FA-Tools\Ant\bin\ant clean dist
GOTO :QUIT

:GameServer
cd ..\FA-GameServer
start /WAIT /B ..\FA-Tools\Ant\bin\ant clean dist
GOTO :QUIT

:LoginServer
cd ..\FA-LoginServer
start /WAIT /B ..\FA-Tools\Ant\bin\ant clean dist
GOTO :QUIT

:ChatServer
cd ..\FA-ChatServer
start /WAIT /B ..\FA-Tools\Ant\bin\ant clean dist
GOTO :QUIT

:QUIT
exit
