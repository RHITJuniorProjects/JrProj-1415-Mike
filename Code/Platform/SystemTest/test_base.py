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
server_path = 'input/metrics.js'

def read_json(path):
    with open(path,'r') as f:
        return json.load(f)

def initialize_database(ref,init_json):
    path = '/'
    ref.delete(path,None)
    ref.patch(path,init_json)

# this is a hack, should probably be replaced.
# it modifies the database in metrics script.
def sync_metric_server(server_path,base_url):
    with open(server_path,'r') as f:
        lines = f.readlines()
    lines = [re.sub(r"'.*'","'"+base_url+"'",line) if line.startswith('var test =') else line for line in lines]
    with open(server_path,'w') as f:
        f.write(''.join(lines))

def start_metric_server(server_path,base_url):
    sync_metric_server(server_path,base_url)
    devnull = open(os.devnull,'w')
    pid = subprocess.Popen(["nodejs",server_path],stdout=devnull)
    time.sleep(20)     # yes, that's seconds.
    return pid

class HenryTestCase(unittest.TestCase):

    def setUp(self):
        self.init_json = read_json(init_json_path)
        self.ref = firebase.FirebaseApplication(base_url)
        initialize_database(self.ref,self.init_json)
        self.nodejs = start_metric_server(server_path,base_url)

    def tearDown(self):
        self.nodejs.terminate()

if __name__ == '__main__':
    unittest.main()
