import sys
import requests
from cx_Freeze import setup, Executable

include_files = [ "cacert.pem" ]

build_exe_options = {"include_files":[(requests.certs.where(),'cacert.pem')]}

setup(  name = "henry",
        version = "0.1.4",
        description = "Super great windows support",
        options = { "build_exe": build_exe_options },
        executables = [Executable("henry.py")])
