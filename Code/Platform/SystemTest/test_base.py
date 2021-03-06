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

def start_commit_server(commit_server_path):
    devnull = open(os.devnull, 'w')
    pid = subprocess.Popen(["nodejs", commit_server_path, "henry-qa"], stdout=devnull)
    time.sleep(5)
    return pid

def start_user_server(user_server_path):
    devnull = open(os.devnull, 'w')
    pid = subprocess.Popen(["nodejs", user_server_path, "henry-qa"], stdout=devnull)
    time.sleep(5)
    return pid

class HenryTestCase(unittest.TestCase):

    def setUp(self):
        self.init_json = read_json(init_json_path)
        self.ref = firebase.FirebaseApplication(base_url)
        initialize_database(self.ref, self.init_json)
        self.nodejs1 = start_commit_server(commit_server_path)
	self.nodejs2 = start_user_server(user_server_path)

    def tearDown(self):
        self.nodejs1.terminate()
	self.nodejs2.terminate()

if __name__ == '__main__':
    unittest.main()
