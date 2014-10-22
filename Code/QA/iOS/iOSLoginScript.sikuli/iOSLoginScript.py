#valid login check
click("1414005987057.png")
type("test1@test.com" + Key.TAB + "test")
click("1414001448736.png")
sleep(10)
if exists("1414001491047.png"):
    print("Login Successful")
else:
    print("ERROR: Login Unsuccessful")

#logout check
click("1414001575740.png")
sleep(3)
click("1414001657851.png")
sleep(10)
if exists("1414001750444.png"):
    print("Logout successful")
else:
    print("ERROR: Logout unsuccessful")

#saving credentials check
if exists("1414001792915.png"):
    print("Credentials correctly saved")
else:
    print("ERROR: Credentials not saved")

#invalid login check
click("1414001973975.png")
type(Key.BACKSPACE + Key.BACKSPACE + Key.BACKSPACE + Key.BACKSPACE + 
        Key.BACKSPACE + Key.BACKSPACE + Key.BACKSPACE + Key.BACKSPACE +
        Key.BACKSPACE + Key.BACKSPACE + Key.BACKSPACE + Key.BACKSPACE +
        Key.BACKSPACE + Key.BACKSPACE)
#14 backspaces...
type("foo@foo.com" + Key.TAB + "foo1")
click("1414002769171.png")
sleep(5)
if exists("1414002796001.png"):
    print("Invalid user check successful")
else:
    print("ERROR: Invalid user check unsuccessful")
