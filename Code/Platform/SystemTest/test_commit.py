import test_base
import util
import time

class CommitTestCase(test_base.HenryTestCase):

    def test_commit(self):
        uid = 'simplelogin:100'
        pid = util.createProject(self.ref,'test commits','','2020-02-20',uid)
        time.sleep(10)
        mid = util.createMilestone(self.ref,pid,'milestone 1','2020-02-20','')
        time.sleep(10)
        tid = util.createTask(self.ref,pid,mid,'task 1','2020-02-20','',1,'')
        time.sleep(10)
        util.commit(self.ref,pid,mid,tid,uid,1,'Testing',2,1)
        time.sleep(10)
        task = util.getTaskInfo(self.ref,pid,mid,tid)
        
        self.assertTrue(task['added_lines_of_code'] == 2)
        self.assertTrue(task['assignedTo'] == uid)
        self.assertTrue(task['due_date'] == '2020-02-20')
        self.assertFalse(task['is_completed'])
        self.assertTrue(task['name'] == 'Task 1')
        self.assertTrue(task['original_hour_estimate'] == 1)
        self.assertTrue(task['percent_complete'] == 100)
        self.assertTrue(task['removed_lines_of_code'] == 1)
        self.assertTrue(task['status'] == 'Testing')
        self.assertTrue(task['total_hours'] == 1)
        self.assertTrue(task['total_lines_of_code'] == 1)

        util.commit(self.ref,pid,mid,tid,uid,2,'Closed',5,1)
        time.sleep(10)
        task = util.getTaskInfo(self.ref,pid,mid,tid)

        self.assertTrue(task['added_lines_of_code'] == 7)
        self.assertTrue(task['assignedTo'] == uid)
        self.assertTrue(task['due_date'] == '2020-02-20')
        self.assertTrue(task['is_completed'])
        self.assertTrue(task['name'] == 'Task 1')
        self.assertTrue(task['original_hour_estimate'] == 1)
        self.assertTrue(task['percent_complete'] == 300)
        self.assertTrue(task['removed_lines_of_code'] == 2)
        self.assertTrue(task['status'] == 'Closed')
        self.assertTrue(task['total_hours'] == 3)
        self.assertTrue(task['total_lines_of_code'] == 5)
