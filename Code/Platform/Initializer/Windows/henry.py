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
import time
import git_utils
import firebase_utils

VERSION = '0.1.4'
OPSYS = 'windows'
firebase_url = 'https://henry-test.firebaseio.com'

cacert = os.path.dirname(os.path.abspath(os.__file__))+'/cacert.pem'

def initialize_git(projectID,opsys):
    # github API 3.0
    #base_url = 'https://api.github.com/repos/RHITJuniorProjects/JrProj-1415-Mike/contents/Code/Platform/Git/Hooks/WindowsEXE/build/exe.win32-2.7/'
    base_url = 'https://github.com/RHITJuniorProjects/JrProj-1415-Mike/raw/master/Code/Platform/Git/Hooks/WindowsEXE/build/exe.win32-2.7/'
    
    files = [ "commit.exe", "cacert.pem", "select.pyd", "_multiprocessing.pyd", "_hashlib.pyd", "pyexpat.pyd", "_ctypes.pyd", "unicodedata.pyd", "_ssl.pyd", "bz2.pyd", "_socket.pyd", "python27.dll", "library.zip" ]

    sh_url = base_url+'/commit-msg'
    py_url = base_url+'/henry/commit.py'

    hook_dir = os.getcwd()+'\\.git\\hooks\\'
    henry_dir = hook_dir+'henry\\'
    
    if not git_utils.inGitRepo():
        print 'HENRY Error: Must be in the root of a Git repository'
        sys.exit(1)
    
    with open(hook_dir+'commit-msg','w') as f:
        f.write('#!/usr/bin/env bash\n.git/hooks/henry/commit.exe $1 "'+projectID+'"')

    if not os.path.exists(henry_dir):
        os.makedirs(henry_dir)

    for my_file in files:
        urllib.urlretrieve(base_url+my_file, os.path.join(henry_dir,my_file))

    print 'HENRY: Repository succesfully connected to Henry'


def fillTemplate(line,projectID):
    if line.startswith('projectID = '):
        return "projectID = '"+projectID+"'"
    else:
        return line


def editHours(ref):
    email = git_utils.getEmail()
    uID = firebase_utils.getUserID(ref,email)
    pID = readProjectID()
    if pID == None:
        print 'Henry repository not initialized or corrupted'
        return
    mID = promptForMilestone(ref,pID)
    if mID == None:
        return
    tID = promptForTask(ref,uID,pID,mID)
    current = firebase_utils.getCurrentHourEstimate(ref,pID,mID,tID)
    update = promptForUpdate(current)
    if update != None:
        firebase_utils.setCurrentHourEstimate(ref,update,uID,pID,mID,tID)


def readProjectID():
    try:
        with open('.git/hooks/commit-msg','r') as f:
            return f.read().strip().split(' ')[-1][1:-1]
    except:
        return None


def promptForMilestone(ref,pID):
    print 'Milestones:'
    milestoneIDs = firebase_utils.getActiveMilestones(ref,None,pID)
    if len(milestoneIDs) == 0:
        print 'No milestones in this project'
        return None
    for index,(mID,mName) in zip(range(len(milestoneIDs)),milestoneIDs.iteritems()):
        print ' - '+str(index)+'. '+mName
    try:
        sys.stdout.write('Milestone: ')
        selection = int(raw_input())
        assert selection >= 0
        assert selection < len(milestoneIDs)
    except:
        print 'Invalid entry, input should be an integer from the list'
        return None
    return list(milestoneIDs)[selection]


def promptForTask(ref,uID,pID,mID):
    print 'Tasks:'
    taskIDs = firebase_utils.getAssignedTasks(ref,uID,pID,mID)
    if len(taskIDs) == 0:
        print 'No tasks assigned to you for this milestone'
        return None
    for index,(tID,tName) in zip(range(len(taskIDs)),taskIDs.iteritems()):
        print ' - '+str(index)+'. '+tName
    try:
        sys.stdout.write('Task: ')
        selection = int(raw_input())
        assert selection >= 0
        assert selection < len(taskIDs)
    except:
        print 'Invalid entry, input should be an integer from the list'
        return None
    return list(taskIDs)[selection]


def promptForUpdate(current):
    print 'Current Hour Estimate: '+str(current)
    sys.stdout.write('Updated Hour Estimate: ')
    try:
        update = int(raw_input())
        assert update >= 0
        return update
    except:
        print 'Invalid number of hours, must be positive number'


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
    if not git_utils.inGitRepo():
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


def status(ref):
    if not git_utils.inGitRepo():
        print 'This is not a Git repository, Henry cannot be initialized here'
    elif os.path.isfile(os.getcwd()+'/.git/hooks/commit-msg'):
        with open (os.getcwd()+'\\.git\\hooks\\commit-msg') as f:
            fullfile = f.read()
        try:
            projectID = fullfile.split('"')[1]
            ref = firebase.FirebaseApplication(firebase_url,None)
            projectName = ref.get('/projects/'+projectID,None)['name']
            print 'Git repository is connected to Henry Project: '+projectName
        except Exception as e:
            # traceback.print_exc(file=sys.stdout)  # useful for debugging
            print 'Git repository is connected to an invalid Henry project, possibly one that has been deleted'
    else:
        print 'This Git repository is not yet connected to Henry'
        print 'Connect it with'
        print '   henry init <project name>'


if __name__ == '__main__':
    multiprocessing.freeze_support()    # this is a windows thing
    ref = firebase.FirebaseApplication(firebase_url,None)
    sys.argv = [v.strip() for v in sys.argv]
    try:
        if len(sys.argv) == 1:
            usage()
        elif sys.argv[1] == 'init':
            try:
                pID = firebase_utils.getProjectID(ref,sys.argv[2]) 
            except:
                traceback.print_exc(file=sys.stdout)
                print 'HENRY Error: Invalid or nonexistant Henry project name'
                print '   henry init <project name>'
                exit(1)
            try:
                email = git_utils.getEmail()
                userID = firebase_utils.getUserID(ref,email)
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
            status(ref)
        elif sys.argv[1] in {'edit','edithours'}:
            editHours(ref)
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