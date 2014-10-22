#valid login check
click("1413862843952.png")
wait(.5)        
click("1413862727594.png")
type("henry-test.firebaseapp.com" + Key.ENTER)
click("1413862956783.png")
click("1413862977026.png")
type("test2@test.com" + Key.TAB + "test2")
click("1413863086054.png")
if exists("1413863276230.png"):
    print("Log in successful")
else:
    print("ERROR: Log in unsuccessful")

#logout check
click("1413863573148.png")
if exists("1413862956783.png"):
    print("Logout successful")
else:
    print("ERROR: Logout unsuccessful")

#invalid login check
click("1413862843952.png")
wait(.5)        
click("1413862727594.png")
type("henry-test.firebaseapp.com" + Key.ENTER)
click("1413862956783.png")
click("1413862977026.png")
type("foo@foo.com" + Key.TAB + "foofoofoo")
click("1413863086054.png")
wait(1)
if exists("1413863452488.png"):
    print("Invalid credentials check successful")
else:
    print("ERROR: Invalid credentials check unsuccessful")