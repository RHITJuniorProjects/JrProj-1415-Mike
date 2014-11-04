from firebase import firebase


def getProjectID(project,ref):
    path = '/projects'
    projects = ref.get(path,None)
    print project
    projects_with_names = {p:projects[p] for p in projects if 'name' in projects[p]}
    print projects_with_names
    try:
        projectID = [p for p in projects_with_names if projects_with_names[p]['name'] == project][0]
    except:
        raise Exception('HENRY: Invalid or nonexistant project name')
    return projectID


def getMilestoneID(projectID,milestone,ref):
    path = '/projects/'+projectID+'/milestones'
    milestones = ref.get(path,None)
    milestones_with_names = {m:milestones[m] for m in milestones if 'name' in milestones[m]}
    try:
        milestoneID = [m for m in milestones_with_names if milestones_with_names[m]['name'] == milestone][0]
    except:
        raise Exception('HENRY: Invalid or nonexistant milestone name')
    return milestoneID


def getUserID(ref,name):
    path = '/users'
    users = ref.get(path,None)
    try:
        userID = [u for u in users if 'name' in users[u] and users[u]['name'] == name][0]
    except:
        raise Exception('HENRY: Invalid or nonexistance user name')
    return userID


def createMilestone(ref,projectID,milestoneName,milestoneDesc):
    path = '/projects/'+projectID+'/milestones'
    ref.post(path,{
        'name':milestoneName,
        'description':milestoneDesc
    })


def createProject(ref,projectName,projectDesc):
    path = '/projects'
    ref.post(path,{
        'name':projectName,
        'description':projectDesc
    })


def createTask(ref,projectID,milestoneID,taskName,taskDesc):
    path = '/projects/'+projectID+'/milestones/'+milestoneID+'/tasks'
    ref.post(path,{
        'name':taskName,
        'description':taskDesc
    })


def createUser(ref,userID,name,email):
    path = '/users'
    ref.post(path,{
        'name':name,
        'email':email
    })
