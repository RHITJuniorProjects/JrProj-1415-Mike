# Adam Michael
# ajmichael0@gmail.com

import unittest
from firebase import firebase
import subprocess
import json
import time
import re
import os


base_url = 'https://henry-qa.firebaseio.com'
init_json_path = 'input/init.json'
commit_server_path = 'input/1_CommitListener.js'
user_server_path = 'input/2_UserListener.js'

def read_json(path):
    with open(path,'r') as f:
        return json.load(f)

def initialize_database(ref,init_json):
    path = '/'
    ref.delete(path,None)
    ref.patch(path,init_json)

# this is a hack, should probably be replaced.
# it modifies the database in metrics script.
#def sync_metric_server(server_path,base_url):
 #   with open(server_path,'r') as f:
  #      lines = f.readlines()
   # lines = [re.sub(r"'.*'","'"+base_url+"'",line) if line.startswith('var test =') else line for line in lines]
    #with open(server_path,'w') as f:
     #   f.write(''.join(lines))

def start_commit_server(commit_server_path):
    #sync_metric_server(server_path,base_url)
    devnull = open(os.devnull,'w')
    pid = subprocess.Popen(["nodejs 1_CommitListener.js henry-qa",commit_server_path],stdout=devnull)
	time.sleep(20)
    return pid

def start_user_server(user_server_path):
	devnull = open(os.devnull, 'w')
	pid = subprocess.Popen(["nodejs 2_UserListener.js henry-qa",user_server_path],stdout=devnull)
    time.sleep(20)
	return pid

class HenryTestCase(unittest.TestCase):

    def setUp(self):
        self.init_json = read_json(init_json_path)
        self.ref = firebase.FirebaseApplication(base_url)
        initialize_database(self.ref,self.init_json)
        self.nodejs1 = start_commit_server(commit_server_path)
		time.sleep(20)
		self.nodejs2 = start_user_server(user_server_path)

    def tearDown(self):
        self.nodejs1.terminate()
		self.nodejs2.terminate()

if __name__ == '__main__':
    unittest.main()
