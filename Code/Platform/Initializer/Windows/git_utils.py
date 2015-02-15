import subprocess
import os

def getEmail():
    command = 'git config --global user.email'.split(' ')
    pipe = subprocess.Popen(command,stdout=subprocess.PIPE)
    return pipe.communicate()[0].strip()


def inGitRepo():
    try:
        with open(os.getcwd()+'/.git/hooks/commit-msg.sample','r') as f:
            pass
        return True
    except IOError:
        return False



