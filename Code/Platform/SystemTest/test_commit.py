import test_base
import util
import time

class CommitTestCase(test_base.HenryTestCase):

    def test_commit(self):
        uid = 'simplelogin:100'
        pid = util.createProject(self.ref,'test commits','','2020-02-20',uid)
        time.sleep(10)
        mid1 = util.createMilestone(self.ref,pid,'milestone 1','2020-02-20','')
        time.sleep(10)
        tid1 = util.createTask(self.ref,pid,mid1,uid,'task 1','2020-02-20','',1,'')
        time.sleep(10)
        tid2 = util.createTask(self.ref,pid,mid1,uid,'task 2', '2020-02-20','',2,'')
        time.sleep(10)
        util.commit(self.ref,pid,mid1,tid1,uid,1,'Testing',2,1)
        time.sleep(10)
        util.commit(self.ref,pid,mid1,tid2,uid,1,'New',3,0)
        time.sleep(10)

        # task in projects table
        task = util.getTaskInfo(self.ref,pid,mid1,tid1)
        self.assertTrue(task['added_lines_of_code'] == 2)
        self.assertTrue(task['assignedTo'] == uid)
        self.assertTrue(task['due_date'] == '2020-02-20')
        # self.assertFalse(task['is_completed'])
        self.assertTrue(task['name'] == 'task 1')
        self.assertTrue(task['original_hour_estimate'] == 1)
        self.assertTrue(task['percent_complete'] == 100)
        self.assertTrue(task['removed_lines_of_code'] == 1)
        self.assertTrue(task['status'] == 'Testing')
        self.assertTrue(task['total_hours'] == 1)
        self.assertTrue(task['total_lines_of_code'] == 1)

        # task in users table
        task = util.getUserTask(self.ref,uid,pid,mid1,tid1)
        self.assertTrue(task['added_lines_of_code'] == 2)
        self.assertTrue(task['points'] == 0)
        self.assertTrue(task['removed_lines_of_code'] == 1)
        self.assertTrue(task['total_hours'] == 1)
        self.assertTrue(task['total_lines_of_code'] == 1)

        # milestone in projects table
        milestone = util.getMilestoneInfo(self.ref,pid,mid1)
        self.assertTrue(milestone['added_lines_of_code'] == 5)
        self.assertTrue(milestone['due_date'] == '2020-02-20')
        self.assertTrue(milestone['hours_percent'] == 66)
        self.assertTrue(milestone['name'] == 'milestone 1')
        self.assertTrue(milestone['removed_lines_of_code'] == 1)
        self.assertTrue(milestone['task_percent'] == 0)
        self.assertTrue(milestone['tasks_completed'] == 0)
        self.assertTrue(milestone['total_estimated_hours'] == 3)
        self.assertTrue(milestone['total_hours'] == 2)
        self.assertTrue(milestone['total_lines_of_code'] == 4)
        self.assertTrue(milestone['total_tasks'] == 2)

        # milestone in users table
        milestone = util.getUserMilestone(self.ref,uid,pid,mid1)
        self.assertTrue(milestone['added_lines_of_code'] == 5)
        self.assertTrue(milestone['added_loc_percent'] == 100)
        self.assertTrue(milestone['hours_percent'] == 100)
        self.assertTrue(milestone['loc_percent'] == 100)
        self.assertTrue(milestone['removed_lines_of_code'] == 1)
        self.assertTrue(milestone['removed_loc_percent'] == 100)
        self.assertTrue(milestone['total_hours'] == 2)
        self.assertTrue(milestone['total_lines_of_code'] == 4)
        self.assertTrue(milestone['total_points'] == 0)

        # project in projects table
        project = util.getProject(self.ref,pid)
        self.assertTrue(project['added_lines_of_code'] == 5)
        self.assertTrue(project['due_date'] == '2020-02-20')
        self.assertTrue(project['hours_percent'] == 66)
        self.assertTrue(project['milestone_percent'] == 0)
        self.assertTrue(project['milestones_completed'] == 0)
        self.assertTrue(project['name'] == 'test commits')
        self.assertTrue(project['removed_lines_of_code'] == 1)
        self.assertTrue(project['task_percent'] == 0)
        self.assertTrue(project['tasks_completed'] == 0)
        self.assertTrue(project['total_estimated_hours'] == 3)
        self.assertTrue(project['total_hours'] == 2)
        self.assertTrue(project['total_lines_of_code'] == 4)
        self.assertTrue(project['total_milestones'] == 1)
        self.assertTrue(project['total_tasks'] == 2)

        # project in users table
        project = util.getUserProject(self.ref,uid,pid)
        self.assertTrue(project['added_lines_of_code'] == 5)
        self.assertTrue(project['added_loc_percent'] == 100)
        self.assertTrue(project['hours_percent'] == 100)
        self.assertTrue(project['loc_percent'] == 100)
        self.assertTrue(project['removed_lines_of_code'] == 1)
        self.assertTrue(project['removed_loc_percent'] == 100)
        self.assertTrue(project['total_hours'] == 2)
        self.assertTrue(project['total_lines_of_code'] == 4)
        self.assertTrue(project['total_points'] == 0)

        util.commit(self.ref,pid,mid1,tid1,uid,2,'Closed',5,1)
        time.sleep(10)

        # task in projects table
        task = util.getTaskInfo(self.ref,pid,mid1,tid1)
        self.assertTrue(task['added_lines_of_code'] == 7)
        # self.assertTrue(task['is_completed'])
        self.assertTrue(task['percent_complete'] == 300)
        self.assertTrue(task['removed_lines_of_code'] == 2)
        self.assertTrue(task['status'] == 'Closed')
        self.assertTrue(task['total_hours'] == 3)
        self.assertTrue(task['total_lines_of_code'] == 5)

        # task in users table
        task = util.getUserTask(self.ref,uid,pid,mid1,tid1)
        self.assertTrue(task['added_lines_of_code'] == 7)
        self.assertTrue(task['points'] == 0)
        self.assertTrue(task['removed_lines_of_code'] == 2)
        self.assertTrue(task['total_hours'] == 3)
        self.assertTrue(task['total_lines_of_code'] == 5)
        
        # milestone in projects table
        milestone = util.getMilestoneInfo(self.ref,pid,mid1)
        self.assertTrue(milestone['added_lines_of_code'] == 10)
        self.assertTrue(milestone['hours_percent'] == 133)
        self.assertTrue(milestone['removed_lines_of_code'] == 2)
        self.assertTrue(milestone['task_percent'] == 50)
        self.assertTrue(milestone['tasks_completed'] == 1)
        self.assertTrue(milestone['total_hours'] == 4)
        self.assertTrue(milestone['total_lines_of_code'] == 8)
        self.assertTrue(milestone['total_tasks'] == 2)

        # milestone in users table
        milestone = util.getUserMilestone(self.ref,uid,pid,mid1)
        self.assertTrue(milestone['added_lines_of_code'] == 10)
        self.assertTrue(milestone['added_loc_percent'] == 100)
        self.assertTrue(milestone['hours_percent'] == 100)
        self.assertTrue(milestone['loc_percent'] == 100)
        self.assertTrue(milestone['removed_lines_of_code'] == 2)
        self.assertTrue(milestone['removed_loc_percent'] == 100)
        self.assertTrue(milestone['total_hours'] == 4)
        self.assertTrue(milestone['total_lines_of_code'] == 8)
        self.assertTrue(milestone['total_points'] == 0)
        
        # project in projects table
        project = util.getProject(self.ref,pid)
        self.assertTrue(project['added_lines_of_code'] == 10)
        self.assertTrue(project['hours_percent'] == 133)
        self.assertTrue(project['milestone_percent'] == 0)
        self.assertTrue(project['milestones_completed'] == 0)
        self.assertTrue(project['removed_lines_of_code'] == 2)
        self.assertTrue(project['task_percent'] == 50)
        self.assertTrue(project['tasks_completed'] == 1)
        self.assertTrue(project['total_hours'] == 4)
        self.assertTrue(project['total_lines_of_code'] == 8)
        self.assertTrue(project['total_milestones'] == 1)
        self.assertTrue(project['total_tasks'] == 2)

        # project in users table
        project = util.getUserProject(self.ref,uid,pid)
        self.assertTrue(project['added_lines_of_code'] == 10)
        self.assertTrue(project['added_loc_percent'] == 100)
        self.assertTrue(project['hours_percent'] == 100)
        self.assertTrue(project['loc_percent'] == 100)
        self.assertTrue(project['removed_lines_of_code'] == 2)
        self.assertTrue(project['removed_loc_percent'] == 100)
        self.assertTrue(project['total_hours'] == 4)
        self.assertTrue(project['total_lines_of_code'] == 8)
        self.assertTrue(project['total_points'] == 0)

        mid2 = util.createMilestone(self.ref,pid,'milestone 2','2020-02-20','')
        time.sleep(10)
        tid3 = util.createTask(self.ref,pid,mid2,uid,'task 0'. '2020-02-20','',2,'')
        time.sleep(10)
        util.commit(self.ref,pid,mid2,tid3,uid,3,'Closed',10,2)
        time.sleep(10)

        # task in projects table
        task = util.getTaskInfo(self.ref,pid,mid2,tid3)
        self.assertTrue(task['added_lines_of_code'] == 10)
        self.assertTrue(task['assignedTo'] == uid)
        self.assertTrue(task['due_date'] == '2020-02-20')
        # self.assertTrue(task['is_completed'])
        self.assertTrue(task['name'] == 'task 0')
        self.assertTrue(task['original_hour_estimate'] == 2)
        self.assertTrue(task['percent_complete'] == 150)
        self.assertTrue(task['removed_lines_of_code'] == 2)
        self.assertTrue(task['status'] == 'Closed')
        self.assertTrue(task['total_hours'] == 3)
        self.assertTrue(task['total_lines_of_code'] == 8)

        # task in users table
        task = util.getUserTask(self.ref,uid,pid,mid2,tid3)
        self.assertTrue(task['added_lines_of_code'] == 10)
        self.assertTrue(task['points'] == 0)
        self.assertTrue(task['removed_lines_of_code'] == 2)
        self.assertTrue(task['total_hours'] == 3)
        self.assertTrue(task['total_lines_of_code'] == 8)

        # milestone in projects table
        milestone = util.getMilestoneInfo(self.ref,pid,mid2)
        self.assertTrue(milestone['added_lines_of_code'] == 10)
        self.assertTrue(milestone['due_date'] == '2020-02-20')
        self.assertTrue(milestone['hours_percent'] == 150)
        self.assertTrue(milestone['name'] == 'milestone 2')
        self.assertTrue(milestone['removed_lines_of_code'] == 2)
        self.assertTrue(milestone['task_percent'] == 100)
        self.assertTrue(milestone['tasks_completed'] == 1)
        self.assertTrue(milestone['total_estimated_hours'] == 2)
        self.assertTrue(milestone['total_hours'] == 3)
        self.assertTrue(milestone['total_lines_of_code'] == 8)
        self.assertTrue(milestone['total_tasks'] == 1)

        # milestone in users table
        milestone = util.getUserMilestone(self.ref,uid,pid,mid2)
        self.assertTrue(milestone['added_lines_of_code'] == 10)
        self.assertTrue(milestone['added_loc_percent'] == 100)
        self.assertTrue(milestone['hours_percent'] == 100)
        self.assertTrue(milestone['loc_percent'] == 100)
        self.assertTrue(milestone['removed_lines_of_code'] == 2)
        self.assertTrue(milestone['removed_loc_percent'] == 100)
        self.assertTrue(milestone['total_hours'] == 3)
        self.assertTrue(milestone['total_lines_of_code'] == 8)
        self.assertTrue(milestone['total_points'] == 0)

        # project in projects table
        project = util.getProject(self.ref,pid)
        self.assertTrue(project['added_lines_of_code'] == 20)
        self.assertTrue(project['hours_percent'] == 150)
        self.assertTrue(project['milestone_percent'] == 50)
        self.assertTrue(project['milestones_completed'] == 1)
        self.assertTrue(project['removed_lines_of_code'] == 4)
        self.assertTrue(project['task_percent'] == 66)
        self.assertTrue(project['tasks_completed'] == 2)
        self.assertTrue(project['total_hours'] == 7)
        self.assertTrue(project['total_lines_of_code'] == 16)
        self.assertTrue(project['total_milestones'] == 2)
        self.assertTrue(project['total_tasks'] == 3)

        # project in users table
        project = util.getUserProject(self.ref,uid,pid)
        self.assertTrue(project['added_lines_of_code'] == 20)
        self.assertTrue(project['added_loc_percent'] == 100)
        self.assertTrue(project['hours_percent'] == 100)
        self.assertTrue(project['loc_percent'] == 100)
        self.assertTrue(project['removed_lines_of_code'] == 4)
        self.assertTrue(project['removed_loc_percent'] == 100)
        self.assertTrue(project['total_hours'] == 7)
        self.assertTrue(project['total_lines_of_code'] == 16)
        self.assertTrue(project['total_points'] == 0)
