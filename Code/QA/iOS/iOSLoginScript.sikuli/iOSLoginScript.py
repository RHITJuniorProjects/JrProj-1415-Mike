#valid login check
click("1414005987057.png")
type("test2@test.com" + Key.TAB + "test")
click("1414001448736.png")
wait(10)
if exists("1414001491047.png"):
    print("Login Successful")
else:
    print("ERROR: Login Unsuccessful")

#logout check
click("1414001575740.png")
wait(3)
click("1414001657851.png")
wait(10)
if exists("1414001750444.png"):
    print("Logout successful")
else:
    print("ERROR: Logout unsuccessful")

#saving credentials check
if exists("1415321345982.png"):
    print("Credentials correctly saved")
else:
    print("ERROR: Credentials not saved")

#invalid login check
click("1415321345982.png")
type(Key.BACKSPACE + Key.BACKSPACE + Key.BACKSPACE + Key.BACKSPACE + 
        Key.BACKSPACE + Key.BACKSPACE + Key.BACKSPACE + Key.BACKSPACE +
        Key.BACKSPACE + Key.BACKSPACE + Key.BACKSPACE + Key.BACKSPACE +
        Key.BACKSPACE + Key.BACKSPACE)
#14 backspaces...
type("foo@foo.com" + Key.TAB + "foo1")
click("1414002769171.png")
wait(5)
if exists("1415321455842.png"):
    print("Invalid user check successful")
else:
    print("ERROR: Invalid user check unsuccessful")
