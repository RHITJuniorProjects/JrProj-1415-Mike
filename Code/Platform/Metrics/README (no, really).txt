The firebase node module is in the node_modules folder, 
    because this is where Node wanted it in Eclipse. 
		That is, in Eclipse you must have 'node_modules/firebase' in the root of the project.

If you just use command-line Node, NPM should put it in the right place, 
    but NPM does not currently install correctly on Windows:
		
    You will need to add a folder called 'npm' in 'C:\Users\<your_username>\AppData\Roaming. 
        After that, run the command 'npm install firebase' in your system's command-line 
				shell (cmd or PowerShell for Windows, or the bash terminal for Linux / Mac).

