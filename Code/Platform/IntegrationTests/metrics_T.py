import unittest
from firebase import firebase
from functools import partial
import json
import util


base_url = 'https://henry-staging.firebaseio.com/'
init_json_path = 'input/init.json'


def constructInitJson(path):
    with open (path,'r') as f:
        return json.load(f)


# function is reassigned below
def initializeDatabase(ref,init_json):
    path ='/'
    ref.delete(path,None)
    ref.patch(path,init_json)


class CreateProjecTestCase(unittest.TestCase):

    def setUp(self):
        self.init_json = constructInitJson(init_json_path)
        self.ref = firebase.FirebaseApplication(base_url)
        initializeDatabase(self.ref,self.init_json)

    def number_of_projects(self):
        return len(self.ref.get('/projects',None))

    def test_add_one_project(self):
        init_num_projects = self.number_of_projects()
        util.createProject(self.ref,'integration test project 1','ignore this')
        final_num_projects = self.number_of_projects()
        self.assertEqual(init_num_projects+1,final_num_projects)
        
class CreateUserTestCase(unittest.TestCase):

    def setUp(self):
        self.init_json = constructInitJson(init_json_path)
        self.ref = firebase.FirebaseApplication(base_url)
        initializeDatabase(self.ref,self.init_json)

    def number_of_users(self):
        return len(self.ref.get('/users',None))
    
    def test_add_one_user(self):
        init_num_users = self.number_of_users()
 

if __name__ == '__main__':
    unittest.main()
