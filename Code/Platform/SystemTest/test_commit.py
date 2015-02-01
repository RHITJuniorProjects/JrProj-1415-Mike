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
        util.commit(self.ref,pid,mid,tid,uid)
        time.sleep(10)
        task = util.getTask(self.ref,pid,mid,tid)
        self.assertTrue(task.original_hour_estimate == 1)
        self.assertTrue(task.due_date == '2020-02-20')
