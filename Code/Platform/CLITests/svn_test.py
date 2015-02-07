from firebase import firebase
import firebase_utils
import svn_utils
import operator
import sys
import re

BASE_URL = 'https://henry-test.firebaseio.com'
REQUIRED_FIELDS = ['email', 'hours', 'project', 'milestone', 'task', 'status']


def commit(ref,message,diff):
    fields = parse(message)
    if not reduce(operator.and_,map(lambda x: x in fields.keys(),REQUIRED_FIELDS)):
        raise Exception('HENRY: Missing fields in commit message')
    uID = firebase_utils.getUserID(ref,fields['email'])
    pID = firebase_utils.getProjectID(ref,fields['project'])
    mID = firebase_utils.getMilestoneID(ref,pID,fields['milestone'])
    tID = firebase_utils.getTaskID(ref,pID,mID,fields['task'])
    firebase_utils.commit(ref,fields['message'],uID,pID,mID,tID,fields['hours'],fields['status'],fields['added_lines_of_code'],fields['removed_lines_of_code'])


def parse(message):
    field_strings = re.findall(r'\[.*?\]',message)
    print field_strings
    parse_list = [map(lambda s: s.strip(),s[1:-1].lower().split(':')) for s in field_strings]
    field_map = {v[0]:v[1] for v in parse_list}
    bare_message = message
    for field_string in field_strings:
        bare_message = bare_message.replace(field_string,'')
    base_message = bare_message.strip()
    field_map['message'] = bare_message
    field_map['added_lines_of_code'],field_map['removed_lines_of_code'] = 0, 0
    print field_map
    return field_map


if __name__ == '__main__':
    ref = firebase.FirebaseApplication(BASE_URL)
    #commit_message = sys.argv[1]
    #diff = sys.argv[2]
    commit_message = 'Modified a text file [hours:2] [project:Henry - Platform] [milestone:Sprint 5 - CLI] [task:Set up an SVN server] [status:Closed] [email:ajmichael0@gmail.com]'
    diff = None
    commit(ref,commit_message,diff)
