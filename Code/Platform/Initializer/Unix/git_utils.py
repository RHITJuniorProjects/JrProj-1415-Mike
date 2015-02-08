#!/usr/bin/python

from base64 import b64decode
from firebase import firebase
import json
import os
import requests
import subprocess
import sys
import traceback
import re
import time

def initialize_git(projectID,opsys):
    # github API 3.0
    base_url = 'https://api.github.com/repos/RHITJuniorProjects/JrProj-1415-Mike/contents/Code/Platform/Git/Hooks'
    if opsys == 'unix':
        base_url = base_url + '/Unix'
    else:
        base_url = base_url + '/Windows'

    sh_url = base_url+'/commit-msg'
    py_urls = ['/commit.py', '/git_utils.py', '/firebase_utils.py', '/__init__.py']

    hook_dir = os.getcwd()+'/.git/hooks'
    henry_dir = hook_dir+'/henry'
    sh_path = hook_dir+'/commit-msg'

    if not inGitRepo():
        print 'HENRY Error: Must be in the root of a Git repository'
        exit()

    # retrieve and decode the shell script
    r = requests.get(sh_url)
    sh = b64decode(json.loads(r.text)['content'])
    
    # write the shell script
    with open(sh_path,'w') as f:
        f.write(sh)
    os.system('chmod +x .git/hooks/commit-msg') 

    if not os.path.exists(henry_dir):
        os.makedirs(henry_dir)

    # retrieve and decode the python scripts
    for py_url in py_urls:
        r = requests.get(base_url+'/henry'+py_url)
        py = b64decode(json.loads(r.text)['content'])
        if py_url == '/commit.py':
            py = '\n'.join([fillGitHookTemplate(line,projectID) for line in py.split('\n')])
        with open(henry_dir+py_url,'w') as f:
            f.write(py)
    print 'HENRY: Repository succesfully connected to Henry'


def fillGitHookTemplate(line,projectID):
    if line.startswith('projectID = '):
        return "projectID = '"+projectID+"'"
    else:
        return line


def inGitRepo():
    try:
        with open(os.getcwd()+'/.git/hooks/commit-msg.sample','r') as f:
            pass
        return True
    except IOError:
        return False


def getGitEmail():
    command = 'git config --global user.email'.split(' ')
    pipe = subprocess.Popen(command,stdout=subprocess.PIPE)
    return pipe.communicate()[0].strip()


def readCommit(path):
    with open(path,'r') as msgfile:
        msg = msgfile.read()
    try:
        return msg.strip().split('\n#')[0]
    except:
        return ''
    

def getLoC():
    command = 'git diff --cached --shortstat'.split(' ')
    pipe = subprocess.Popen(command,stdout=subprocess.PIPE)
    pair = pipe.communicate()[0]
    vals = pair.replace(' ','').split(',') 
    if len(vals) == 3:
        vals = vals[1:]
    elif len(vals) == 2:
        vals = [vals[1]]+['0'] if 'insertion' in pair else ['0']+[vals[1]]
    elif len(vals) == 1:
        raise Exception('HENRY: Unexpected output of `git diff --cached --shortstat`')
    else:
        vals = ['0','0']
    nums = map(lambda x: int(x[0]),vals) 
    return nums[0],nums[1]


def parse(msg):
    hoursRE = r"\[hours:([0-9]+)\]"
    milestoneRE = r"\[milestone:([^\]]*)\]"
    taskRE = r"\[task:([^\]]*)\]"
    statusRE = r"\[status:([^\]]*)\]"

    rexs = [hoursRE,milestoneRE,taskRE,statusRE]
    results = [re.search(rex,msg) for rex in rexs]
    results = [res.group(1) if res != None else None for res in results]
    return results


def getEmail():
    command = 'git config --global user.email'.split(' ')
    pipe = subprocess.Popen(command,stdout=subprocess.PIPE)
    return pipe.communicate()[0].strip()
    

def getUserID(email,ref):
    users = ref.get('/users',None)
    filteredusers = {u:users[u] for u in users if 'email' in users[u]}
    try:
        userID = [u for u in filteredusers if filteredusers[u]['email']==email][0]
    except:
        print 'HENRY: Invalid or nonexistant email, commit failed'
        exit(1)
    return userID


getTime = lambda: int(time.time()*1000)


def getMilestoneID(projectID,milestone):
    path = '/projects/'+projectID+'/milestones'
    milestones = ref.get(path,None)
    filtered = {m:milestones[m] for m in milestones if 'name' in milestones[m]}
    try:
        mID = [m for m in filtered if filtered[m]['name']==milestone][0]
    except:
        print 'HENRY: Invalid or nonexistent milestone, commit failed'
        exit(1)
    return mID


def getTaskID(projectID,milestoneID,task):
    path = '/projects/'+projectID+'/milestones/'+milestoneID+'/tasks'
    tasks = ref.get(path,None)
    filtered = {t:tasks[t] for t in tasks if 'name' in tasks[t]}
    try:
        tID = [t for t in filtered if filtered[t]['name']==task][0]
    except:
        print 'HENRY: Invalid or nonexistant task, commit failed'
        exit(1)
    return tID


def writeCommit(ref,msg,project,uid,hours,status,pos_loc,neg_loc,ts,projectID,milestoneID,taskID):
    path = '/commits/'+projectID+'/'
    try:
        result = ref.post(path,{
            'hours':hours,
            'user':uid,
            'added_lines_of_code':pos_loc,
            'removed_lines_of_code':neg_loc,
            'message':msg,
            'timestamp':ts,
            'milestone':milestoneID,
            'task':taskID,
            'status':status,
            'project':projectID
        })
    except:
        raise Exception('Connection to Firebase commits table denied')
    return result.values()[0]


def addCommitToUser(ref,uid,commitid):
    path = '/users/' + uid + '/commits'
    try:
        ref.patch(path,{commitid:commitid})
    except:
        raise Exception('Connection to Firebase users table denied')


def addCommitToProject(ref,projectid,commitid):
    path = '/projects/'+projectid+'/commits'
    try:
        ref.patch(path,{commitid:commitid})
    except:
        raise Exception('Connection to Firebase projects table denied')
   

def getActiveMilestones(ref,userID,projectID):
    path = '/projects/'+projectID+'/milestones'
    milestones = ref.get(path,None)
    filtered = {m:milestones[m]['name'] for m in milestones if 'name' in milestones[m]}
    return filtered


def getAssignedTasks(ref,userID,projectID,milestoneID):
    path = '/users/'+userID+'/projects/'+projectID+'/milestones/'+milestoneID+'/tasks'
    try:
        taskIDs = ref.get(path,None).keys()
    except:
        print 'HENRY: You have no assigned tasks for this milestone, commit failed'
        exit(1)
    path = '/projects/'+projectID+'/milestones/'+milestoneID+'/tasks'
    allTasks = ref.get(path,None)
    assignedTasks = {tID:allTasks[tID]['name'] for tID in taskIDs}
    return assignedTasks


def getMilestone(ref,projectID,milestoneID):
    return ref.get('/projects/'+projectID+'/milestones/'+milestoneID+'/name',None)


def getTask(ref,projectID,milestoneID,taskID):
    return ref.get('/projects/'+projectID+'/milestones/'+milestoneID+'/tasks/'+taskID+'/name',None)

    
def promptAsNecessary(ref,userID,projectID,hours,milestone,task,status):
    # mac/linux
    sys.stdin = open('/dev/tty')

    # windows
    #sys.stdin = open('CON')

    def_mID, def_tID, def_status = getDefaults()

    if hours == None:
        sys.stdout.write('Hours: ')
        sys.stdout.flush()
        hours = raw_input()

    # prompt for milestone if necessary
    if milestone == None:
        if def_mID != None:
            def_milestone = getMilestone(ref,projectID,def_mID)
            print 'Active milestones (defaults to '+def_milestone+'):'
        else:
            print 'Active milestones:'
        sys.stdout.flush()
        idsByIndex = [] ; index = 1
        for mID, mName in getActiveMilestones(ref,userID,projectID).iteritems():
            print ' - '+str(index)+'. '+mName
            idsByIndex = idsByIndex + [mID]
            index = index + 1
        sys.stdout.write('Milestone: ')
        sys.stdout.flush()
        milestone = raw_input()
        if milestone.isdigit() and int(milestone) <= len(idsByIndex):
            mID = idsByIndex[int(milestone)-1]
        elif milestone == '' and def_mID != 0:
            mID = def_mID
        else:
            mID = getMilestoneID(projectID,milestone)
    else:
        mID = getMilestoneID(projectID,milestone)

    # prompt for task if necessary
    if task == None:
        if def_tID != None:
            def_task = getTask(ref,projectID,mID,def_tID)
            print 'Tasks assigned to you (defaults to '+def_task+'):'
        else:
            print 'Tasks assigned to you:'
        idsByIndex = [] ; index = 1
        for tID, tName in getAssignedTasks(ref,userID,projectID,mID).iteritems():
            print ' - '+str(index)+'. '+tName
            idsByIndex = idsByIndex + [tID]
            index = index + 1
        sys.stdout.write('Task: ')
        sys.stdout.flush()
        task = raw_input()
        if task.isdigit() and int(task) <= len(idsByIndex):
            tID = idsByIndex[int(task)-1]
        elif task == '' and def_tID != 0:
            tID = def_tID
        else:
            tID = getTaskID(projectID,mID,task)
    else:
        tID = getTaskID(projectID,mID,task)

    # prompt for status if necessary
    if status == None:
        # New, Implementation, Testing, Verify, Regression, Closed'
        if def_status != None:
            print 'Select status (defaults to '+def_status+'):'
        else:
            print 'Select status:'
        possibleStatuses = ['New', 'Implementation', 'Testing', 'Verify', 'Regression', 'Closed']
        for i in range(len(possibleStatuses)):
            print ' - ' + str(i + 1) + ': ', possibleStatuses[i]
        sys.stdout.write('Status: ')
        sys.stdout.flush()
        status = raw_input()
        if status.isdigit() and (int(status) <= len(possibleStatuses)):
            status = possibleStatuses[int(status) - 1]
        elif status == '' and def_status != 0:
            status = def_status
        else:
            print 'HENRY: Invalid status, commit failed'
            exit(1)

    return hours,mID,tID,status


def updateDefaults(defaultpath,milestoneID,taskID,status):
    with open(defaultpath,'w+') as f:
        f.write(milestoneID+'\t'+taskID+'\t'+status)


# returns milestoneID, taskID, status
def getDefaults(defaultpath):
    try:
        with open(defaultpath,'r') as f:
            return f.read().strip().split('\t')
    except:
        return None,None,None
