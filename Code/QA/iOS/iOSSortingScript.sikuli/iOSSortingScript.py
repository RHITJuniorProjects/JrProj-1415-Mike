#sorting test script
#logged in as "test2@test.com" "test"
#starting at home screen

click("1415332642234.png")
if exists("1415332705007.png"):
    print("Sorted Z-A successfully")
else:
    print("ERROR: Sorted Z-A unsuccessfully")
click("1415332759178.png")
if exists("1415332804175.png"):
    print("Sorted A-Z successfully")
else:
    print("ERROR: Sorted A-Z unsuccessfully")