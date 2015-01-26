##Test being able to see how many points i have on my profile.
##Tests assume user already logged in.
click("1422304377746.png")
if exists("1422304394995.png"):
    print("It worked")
else:
    print("It failed")
click("1422304425318.png")
