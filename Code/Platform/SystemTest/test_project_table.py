import test_base
import util
import time

class ProjectTableTestCase(test_base.HenryTestCase):

    def test_project_creation(self):
        uid = 'simplelogin:110'
        pid = util.createProject(self.ref,'Yet Another Project','','2020-02-20',uid)
        time.sleep(5)
        projects = util.getProjects(self.ref,uid)
        self.assertTrue(pid in projects)
