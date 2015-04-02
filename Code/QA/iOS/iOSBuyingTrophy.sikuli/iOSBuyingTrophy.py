click("1427938411948.png")
if exists("1427938436943.png"):
    print("Displaying correct points.")
else:
    print ("ERROR: Incorrectly displaying points")

click("1427938535414.png")
click("1427938553160.png")
click("1427938570238.png")
click("1427938584879.png")
if exists("1427938612177.png"):
    print("Trophy purchased correctly")
else:
    print ("ERROR: Trophy not displaying on Profile")


click("1427938570238.png")
click("1427938411948.png")
click("1427938880818.png")
if exists("1427938904074.png"):
    print("Correctly stopped from buying trophy too expensive")
else:
    print ("ERROR: Trophy shouldn't be purchaseable")

