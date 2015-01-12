apt-get install python2.7
easy_install pip
pip install requests
pip install python-firebase
wget -O /usr/bin/lib/henry https://raw.githubusercontent.com/RHITJuniorProjects/JrProj-1415-Mike/master/Code/Platform/Initializer/UnixWithSvn/henry
wget -O /usr/bin/lib/__init__.py https://raw.githubusercontent.com/RHITJuniorProjects/JrProj-1415-Mike/master/Code/Platform/Initializer/UnixWithSvn/__init__.py
wget -O /usr/bin/lib/firebase_utils.py https://raw.githubusercontent.com/RHITJuniorProjects/JrProj-1415-Mike/master/Code/Platform/Initializer/UnixWithSvn/firebase_utils.py
wget -O /usr/bin/lib/git_utils.py https://raw.githubusercontent.com/RHITJuniorProjects/JrProj-1415-Mike/master/Code/Platform/Initializer/UnixWithSvn/git_utils.py
wget -O /usr/bin/lib/svn_utils.py https://raw.githubusercontent.com/RHITJuniorProjects/JrProj-1415-Mike/master/Code/Platform/Initializer/UnixWithSvn/svn_utils.py
ln -s /usr/bin/henry /usr/lib/henry

