##Test android for the ability to see lines of Code
##Must manually change inPasswordValid() in LoginActivity to >`1
click("1415310830433.png")
type("test2@test.com" + Key.TAB + "test")
click("1415310894936.png")
wait(10)
click("1415311071295.png")
wait(5)
click("1415311436005.png")
wait(5)
click("1415311454889.png")
if exists("1415311548396.png"):
    print("Lines of code found.")
else:
    print("No lines of code found.")

