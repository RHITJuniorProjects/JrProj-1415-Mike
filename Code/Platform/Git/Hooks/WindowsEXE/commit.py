#!/usr/bin/python
import os
import sys
import re
import subprocess
import multiprocessing
import time
from firebase import firebase
import traceback


##
#   These fields are populated by the initializer
##
#projectID = '-JYcg488tAYS5rJJT4Kh'

prodUrl = 'https://henry-production.firebaseio.com'
stagUrl = 'https://henry-staging.firebaseio.com'
testUrl = 'https://henry-test.firebaseio.com'

defaultpath = '.git/.henrydefaults'

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
    nums = map(lambda y: int(filter(lambda x: x.isdigit(),y)),vals)
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
        os._exit(0)
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
        os._exit(0)
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
    try:
        # git bash
        sys.stdin = open('CON')
    except IOError:
        # mac/linux/cygwin
        sys.stdin = open('/dev/tty')

    def_mID, def_tID, def_status = getDefaults()

    if getMilestone(ref,projectID,def_mID) == None:
        def_mID, def_tID, def_status == None, None, None

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
        if def_tID != None and mID == def_mID:
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
        if def_status != None and def_tID == tID:
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


def updateDefaults(milestoneID,taskID,status):
    with open(defaultpath,'w+') as f:
        f.write(milestoneID+'\t'+taskID+'\t'+status)


# returns milestoneID, taskID, status
def getDefaults():
    try:
        with open(defaultpath,'r') as f:
            return f.read().strip().split('\t')
    except:
        return None,None,None


if __name__ == '__main__':
    multiprocessing.freeze_support()
    projectID = sys.argv[2]
    # set this to the correct database
    ref = firebase.FirebaseApplication(testUrl, None)

    email = getEmail()
    userID = getUserID(email,ref)
    msg = readCommit(sys.argv[1])
    [hours,milestone,task,status] = parse(msg)
    pos_loc,neg_loc = getLoC()
    ts = getTime()
    hours,milestoneID,taskID,status = promptAsNecessary(ref,userID,projectID,hours,milestone,task,status)

    updateDefaults(milestoneID,taskID,status)

    commitID =writeCommit(ref,msg,None,userID,float(hours),status,pos_loc,neg_loc,ts,projectID,milestoneID,taskID)
    
    #addCommitToProject(ref,projectID,commitID)
    #addCommitToUser(ref,userID,commitID)

    # This bypasses exit handlers to skip the Firebase-Windows errors
    os._exit(0)
