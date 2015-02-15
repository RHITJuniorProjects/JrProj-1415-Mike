def getProjectID(ref,project):
    path = '/projects'
    projects = ref.get(path,None)
    projects_with_names = {p:projects[p] for p in projects if 'name' in projects[p]}
    try:
        projectID = [p for p in projects_with_names if projects_with_names[p]['name'] == project][0]
    except:
        raise Exception('HENRY: Invalid or nonexistant project name')
    return projectID


def getUserID(ref,email):
    path = '/users'
    users = ref.get(path,None)
    filteredusers = {u:users[u] for u in users if 'email' in users[u]}
    try:
        userID = [u for u in filteredusers if filteredusers[u]['email'] == email][0]
    except:
        raise Exception('HENRY: Invalid username')
    return userID


def setCurrentHourEstimate(ref,hours,uID,pID,mID,tID):
    path = '/commits/'+pID
    ref.post(path,{
        'added_lines_of_code':0,
        'hours':0,
        'message':'direct change to task from command line',
        'milestone':mID,
        'project':pID,
        'removed_lines_of_code':0,
        'status':'New',  # This is a bug, bonus points to whoever fixes it
        'task':tID,
        'timestamp':int(time.time()*1000),
        'updated_hour_estimate':hours,
        'user':uID
    })


def getCurrentHourEstimate(ref,pID,mID,tID):
    path = '/projects/'+pID+'/milestones/'+mID+'/tasks/'+tID
    return ref.get(path,None)['updated_hour_estimate']


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
