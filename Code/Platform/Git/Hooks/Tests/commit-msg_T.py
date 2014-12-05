import unittest
import os
import commit as hook

class ParseTestCase(unittest.TestCase):
    
    def test_all_present(self):
        msg = 'abc [hours:2] [milestone:Milestone 32] [task:Fill out GoogleDoc] [status:New]'
        expected = ['2','Milestone 32','Fill out GoogleDoc','New']
        actual = hook.parse(msg)
        self.assertEqual(expected,actual)

    def test_missing_hours(self):
        msg = 'abc [milestone:Milestone 32] [task:Fill out GoogleDoc] [status:New]'
        expected = [None,'Milestone 32','Fill out GoogleDoc','New']
        actual = hook.parse(msg)
        self.assertEqual(expected,actual)

    def test_missing_milestone(self):
        msg = 'abc [hours:2] [task:Fill out GoogleDoc] [status:New]'
        expected = ['2',None,'Fill out GoogleDoc','New']
        actual = hook.parse(msg)
        self.assertEqual(expected,actual)
    
    def test_missing_task(self):
        msg = 'abc [hours:2] [milestone:Milestone 32] [status:New]'
        expected = ['2','Milestone 32',None,'New']
        actual = hook.parse(msg)
        self.assertEqual(expected,actual)

    def test_missing_status(self):
        msg = 'abc [hours:2] [milestone:Milestone 32] [task:Fill out GoogleDoc]'
        expected = ['2','Milestone 32','Fill out GoogleDoc',None]
        actual = hook.parse(msg)
        self.assertEqual(expected,actual)

    def test_missing_all(self):
        msg = 'abc'
        expected = [None,None,None,None]
        actual = hook.parse(msg)
        self.assertEqual(expected,actual)


class EmailExtractionTestCase(unittest.TestCase):

    # WARNING: If this test fails, you may need to reset your Git email
    def test_extract_email_from_git(self):
        original = hook.getEmail()
        expected = 'test@test.com'
        self.set_git_email(expected)
        actual = hook.getEmail()
        self.assertEqual(expected,actual)
        self.set_git_email(original)

    def set_git_email(self,email):
        os.system('git config --global user.email '+email)

if __name__ == '__main__':
    unittest.main()
