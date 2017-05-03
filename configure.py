import os
import re
import fileinput
import sys

class MyClass(object):
    def __init__(self):
        self.configure()

    def configure(self):
        cwd = os.getcwd()
        
        prop_dir = '\\src\\main\\resources'
        app_props_file = cwd + prop_dir + '\\application.properties'
        app_source_file = cwd + prop_dir + '\\application_source.properties'
        
        props_file = cwd + '\\configure.properties'
        
        print("Configuring application...")
        
        tokens = {}
        prop_lines = []
 
        try:
            with open(props_file) as f:
                for line in f:
                    token_match = re.search('^(.*)=(.*)', line)
                    tokens[token_match.group(1)] = token_match.group(2)
        except IOError as e:
            print('ERROR: Could not find a \'configure.properties\' file!\n'
                  + 'Create one in the base directory following this format:\n'
                  + '\tMONGO_URI=<mongo_db_address>')
            sys.exit()
            
        
        with open(app_source_file) as f:
            for line in f:
                for token in tokens:
                    if token in line:
                        print('=> Replacing ' + token + ' with ' + tokens[token])
                        prop_lines.append(line.replace(token, tokens[token]) +'\n')
        
        
        f = open(app_props_file, 'w+')
        f.seek(0)
        
        for line in prop_lines:
            f.write(line)
        
        f.close()
        

if __name__ == '__main__':
    instance = MyClass()
