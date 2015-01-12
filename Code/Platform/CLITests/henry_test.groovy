import groovy.util.GroovyTestCase

class HelpMenuTest extends GroovyTestCase {

  void testHelpCommand() {
    def command = """henry help"""
    def proc = command.execute()
    proc.waitFor()
    def output = proc.in.text
    assert output.contains("Connect a Git repository to Henry with")
    assert output.contains("Henry commit data can be entered in-line with the command")
  }

  void testAbbreviation() {
    def command = """henry -h"""
    def proc = command.execute()
    proc.waitFor()
    def output = proc.in.text
    assert output.contains("Connect a Git repository to Henry with")
    assert output.contains("Henry commit data can be entered in-line with the command")
  }

}
