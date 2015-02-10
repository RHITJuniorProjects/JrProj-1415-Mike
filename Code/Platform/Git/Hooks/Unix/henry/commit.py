#!/usr/bin/python
import os
import sys
import re
import subprocess
import time
from firebase import firebase
import firebase_utils
import git_utils


##
#   This field is populated by the initializer
#   Any change will be overwritten
##
projectID = '-JYcg488tAYS5rJJT4Kh'
databaseUrl = 'https://henry-test.firebaseio.com'
defaultpath = '.git/.henrydefaults'


def promptAsNecessary(ref,userID,projectID,hours,milestone,task,status,email):
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
            def_milestone = firebase_utils.getMilestone(ref,projectID,def_mID)
            print 'Active milestones (defaults to '+def_milestone+'):'
        else:
            print 'Active milestones:'
        sys.stdout.flush()
        idsByIndex = [] ; index = 1
        for mID, mName in firebase_utils.getActiveMilestones(ref,userID,projectID).iteritems():
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
            mID = firebase_utils.getMilestoneID(ref,projectID,milestone)
    else:
        mID = firebase_utils.getMilestoneID(ref,projectID,milestone)

    # prompt for task if necessary
    if task == None:
        if def_tID != None and mID == def_mID:
            def_task = firebase_utils.getTask(ref,projectID,mID,def_tID)
            print 'Tasks assigned to you (defaults to '+def_task+'):'
        else:
            print 'Tasks assigned to you:'
        idsByIndex = [] ; index = 1
        for tID, tName in firebase_utils.getAssignedTasks(ref,userID,projectID,mID).iteritems():
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
            tID = firebase_utils.getTaskID(projectID,mID,task)
    else:
        tID = firebase_utils.getTaskID(projectID,mID,task)

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

    sys.stdout.write('Pair programmed? (y/n): ')
    sys.stdout.flush()
    response = raw_input()
    if response.lower()[0] == 'y':
        team_emails = firebase_utils.teamEmails(ref,projectID)
        team_emails = [e for e in team_emails if e != email]
        if not team_emails:
            print 'HENRY: No team members, are you sure you pair programmed?'
            exit(1)
        index = 1
        for e in team_emails:
            print ' - '+str(index)+'. '+e
            index = index+1
        sys.stdout.write('Partner: ')
        sys.stdout.flush()
        partner = raw_input()
        if partner.isdigit() and int(partner) <= len(team_emails):
            pp = firebase_utils.getUserID(ref,team_emails[int(partner)-1])
        elif partner in team_emails:
            pp = firebase_utils.getUserID(ref,partner)
        else:
            print 'HENRY: Invalid team member'
            exit(1)
    else:
        pp = None

    return hours,mID,tID,status,pp


# honestly, this default values stuff was a mistake
# its a minor feature, but causes 95% of errors
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
    ref = firebase.FirebaseApplication(databaseUrl, None)

    email = git_utils.getEmail()
    userID = firebase_utils.getUserID(ref,email)
    msg = git_utils.readCommit(sys.argv[1])
    [hours,milestone,task,status] = git_utils.parse(msg)
    pos_loc,neg_loc = git_utils.getLoC()
    ts = int(time.time()*1000)  # time.time() returns seconds, so ts is in ms (which is the standard)
    hours,milestoneID,taskID,status,pp = promptAsNecessary(ref,userID,projectID,hours,milestone,task,status,email)

    updateDefaults(milestoneID,taskID,status)

    firebase_utils.writeCommit(ref,msg,None,userID,float(hours),status,pos_loc,neg_loc,ts,projectID,milestoneID,taskID)
    if pp != None:
        firebase_utils.writeCommit(ref,msg,None,pp,float(hours),status,pos_loc,neg_loc,ts,projectID,milestoneID,taskID)

    # This bypasses exit handlers to skip the Firebase-Windows errors
    os._exit(0)
