#!/usr/bin/python

from base64 import b64decode
from firebase import firebase
import json
import os
import requests
import subprocess
import sys
from sys import exit
import traceback
import re
import multiprocessing
import urllib

VERSION = '0.1.4'
OPSYS = 'windows'
firebase_url = 'https://henry-test.firebaseio.com'

cacert = os.path.dirname(os.path.abspath(os.__file__))+'/cacert.pem'

def initialize_git(projectID,opsys):
    # github API 3.0
    #base_url = 'https://api.github.com/repos/RHITJuniorProjects/JrProj-1415-Mike/contents/Code/Platform/Git/Hooks/WindowsEXE/build/exe.win32-2.7/'
    base_url = 'https://github.com/RHITJuniorProjects/JrProj-1415-Mike/raw/master/Code/Platform/Initializer/WindowsEXE/build/exe.win32-2.7/'
    
    files = [ "commit.exe", "cacert.pem", "select.pyd", "_multiprocessing.pyd", "_hashlib.pyd", "pyexpat.pyd", "_ctypes.pyd", "unicodedata.pyd", "_ssl.pyd", "bz2.pyd", "_socket.pyd", "python27.dll", "library.zip" ]

    sh_url = base_url+'/commit-msg'
    py_url = base_url+'/henry/commit.py'

    hook_dir = os.getcwd()+'\\.git\\hooks\\'
    henry_dir = hook_dir+'henry\\'
    
    if not inGitRepo():
        print 'HENRY Error: Must be in the root of a Git repository'
        sys.exit(1)
    
    
    #r = requests.get(base_url+'commit-msg', verify=sys.argv[0].replace('henry.exe','cacert.pem'))
    #s = b64decode(json.loads(r.text)['content'])
    #with open(hook_dir+'commit-msg','w') as f:
    #    f.write(s)
    urllib.urlretrieve(base_url+'commit-msg', hook_dir+'commit-msg')
    os.system('chmod +x .git/hooks/commit-msg')

    if not os.path.exists(henry_dir):
        os.makedirs(henry_dir)

    for my_file in files:
        urllib.urlretrieve(base_url+my_file, os.path.join(henry_dir,my_file))

    #for my_file in files:
    #    print base_url+my_file
    #    r = requests.get(base_url+my_file, verify=sys.argv[0].replace('henry.exe','cacert.pem'))
    #    s = b64decode(json.loads(r.text)['content'])
    #    #with open(henry_dir+my_file) as f:
    #    with open(os.path.join(henry_dir,my_file),'w') as f:
    #        f.write(s)


    print 'HENRY: Repository succesfully connected to Henry'


def fillTemplate(line,projectID):
    if line.startswith('projectID = '):
        return "projectID = '"+projectID+"'"
    else:
        return line

    
def usage():
    print 'HENRY Error: invalid command'
    print
    print 'usage: henry <command> [<args>]' 
    print
    print 'The most commonly used henry commands are:'
    print '   init        Conect the current git repository to Henry'
    print '   version     Display the installed version number'
    print '   help        Display the help menu'
    print '   status      Determines if Henry is initialized in this directory'

def helpmenu():
    if not inGitRepo():
        print 'Henry can only be used within a Git reposity'
        print 'Change directory to a Git repository or create a new one with'
        print '   git init'
        print
    print 'Connect a Git repository to Henry with'
    print '   henry init <project name>'
    print 
    print 'Henry commit data can be entered in-line with the command'
    print "    git commit -m 'my commit message [hours:2] [milestone:Milestone 0] [task:Create database] [status:Regression]'"
    print
    print 'Henry\'s connection information can be displayed with the command'
    print '    henry status'


def version():
    print 'henry version',str(VERSION)


def status():
    if not inGitRepo():
        print 'This is not a Git repository, Henry cannot be initialized here'
    elif os.path.isfile(os.getcwd()+'/.git/hooks/commit-msg'):
        with open (os.getcwd()+'/.git/hooks/henry/commit.py') as f:
            fullfile = f.read()
        try:
            projectID = re.findall(r"projectID = '([-\w]+)'",fullfile)[0]
            ref = firebase.FirebaseApplication(firebase_url,None)
            projectName = ref.get('/projects/'+projectID,None)['name']
            print 'Git repository is connected to Henry Project: '+projectName
        except Exception as e:
            traceback.print_exc(file=sys.stdout)
            print 'Git repository is connected to an invalid Henry project, possibly one that has been deleted'
    else:
        print 'This Git repository is not yet connected to Henry'
        print 'Connect it with'
        print '   henry init <project name>'


def inGitRepo():
    try:
        with open(os.getcwd()+'/.git/hooks/commit-msg.sample','r') as f:
            pass
        return True
    except IOError:
        return False


def getProjectID(project):
    ref = firebase.FirebaseApplication(firebase_url,None)
    path = '/projects'
    projects = ref.get(path,None)
    projects_with_names = {p:projects[p] for p in projects if 'name' in projects[p]}
    try:
        projectID = [p for p in projects_with_names if projects_with_names[p]['name'] == project][0]
    except:
        raise Exception('HENRY: Invalid or nonexistant project name')
    return projectID


def getUserID(email):
    ref = firebase.FirebaseApplication(firebase_url,None)
    path = '/users'
    users = ref.get(path,None)
    filteredusers = {u:users[u] for u in users if 'email' in users[u]}
    try:
        userID = [u for u in filteredusers if filteredusers[u]['email'] == email][0]
    except:
        raise Exception('HENRY: Invalid username')
    return userID


def getEmail():
    command = 'git config --global user.email'.split(' ')
    pipe = subprocess.Popen(command,stdout=subprocess.PIPE)
    return pipe.communicate()[0].strip()


if __name__ == '__main__':
    multiprocessing.freeze_support()

    sys.argv = [v.strip() for v in sys.argv]
    try:
        if len(sys.argv) == 1:
            usage()
        elif sys.argv[1] == 'init':
            try:
                pID = getProjectID(sys.argv[2]) 
            except:
                traceback.print_exc(file=sys.stdout)
                print 'HENRY Error: Invalid or nonexistant Henry project name'
                print '   henry init <project name>'
                exit(1)
            try:
                email = getEmail()
                userID = getUserID(email)
            except:
                traceback.print_exc(file=sys.stdout)
                print 'HENRY Error: Invalid or unregistered Git email address'
                print '   henry init <project name>'
                exit(1)
            initialize_git(pID,OPSYS)
        elif sys.argv[1] in {'version','-version','--version','-v'}:
            version()
        elif sys.argv[1] in {'help','-help','--help','-h'}:
            helpmenu()
        elif sys.argv[1] in {'status'}:
            status()
        else:
            usage()

    except Exception as e:
        traceback.print_exc(file=sys.stdout)
        """
        This line causes the script to exit abruptly and bypass all
        exit handlers. This is necessary because the Python Firebase
        API does not play nice with the Windows Python Multithreading
        library. Maybe someday they will fix it and this won't be
        necessary.
        """
        os._exit(0)
    os._exit(0)
