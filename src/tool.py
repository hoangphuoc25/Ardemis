#!python3

import clint
from clint.textui import prompt, validators
import argparse
import os
import re

command = ''
parser = argparse.ArgumentParser(description='Short sample app')
parser.add_argument('-a', action="store", nargs='+', dest="b")
parser.add_argument('-f', action="store", dest="name")
parser.add_argument('-addm', action="store", nargs='+', dest="method")
parser.add_argument('-adda', action="store", nargs='+', dest="attr")

project_folder 		= os.getcwd()
model_path 			= project_folder + os.sep + 'rd' + os.sep + 'dto'
model_packge 		= 'rd.dto'
dao_interface_path 	= project_folder + os.sep + 'rd' + os.sep + 'spec' + os.sep + 'dao'
dao_interface_package = 'rd.spec.dao'
dao_impl_path 		= project_folder + os.sep + 'rd' + os.sep + 'impl' + os.sep + 'dao'
dao_impl_package	= 'rd.impl.dao'

service_interface_path 		= project_folder + os.sep + 'rd' + os.sep + 'spec' + os.sep + 'service'
service_interface_package 	= 'rd.spec.service'
service_impl_path 			= project_folder + os.sep + 'rd' + os.sep + 'impl' + os.sep + 'service'
service_impl_package 		= 'rd.impl.service'

model_file_tag 				= 'Dto.java'
dao_interface_file_tag 		= 'Dao.java'
dao_impl_file_tag 			= 'DaoImpl.java'
service_interface_file_tag 	= 'Service.java'
service_impl_file_tag 		= 'ServiceImpl.java'

ser_impl_method = '\t\t// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE\n\
\t\ttry{{\n\
\t\t\ttransaction.begin();\n\
\t\t\t{2} {0}.{1}({3});\n\
\t\t\ttransaction.commit();\n\
\t\t\t{4}\
\t\t}} catch (IOException e) {{\n\
\t\t\ttransaction.rollback();\n\
\t\t\tthrow e;\n\
\t\t}}\n'

dao_impl_method = '\t\t// TODO: STUB CODE, MUST MODIFY, DELETE THIS LINE WHEN DONE\n\
\t\tPreparedStatement prepareStatement = null;\n\
\t\tResultSet resultSet = null;\n\
\n\
\t\ttry {{\n\
\t\t\tConnection connection = transaction.getResource(Connection.class);\n\
\t\t\tprepareStatement = connection.prepareStatement({0});\n\
\t\t\tprepareStatement.setString(, );\n\
\t\t\tresultSet = prepareStatement.executeQuery();\n\
\
\t\t\twhile (resultSet.next()) {{\n\
\t\t\t}}\n\
\t\t\t{1}\n\
\t\t}} catch (SQLException e) {{\n\
\t\t\tthrow new IOException(e);\n\
\t\t}} finally {{\n\
\t\t\tif (resultSet != null) {{\n\
\t\t\t\ttry {{\n\
\t\t\t\t\tresultSet.close();\n\
\t\t\t\t}} catch (SQLException e) {{\n\
\t\t\t\t\tlogger.warn(e.getMessage(), e);\n\
\t\t\t\t}}\n\
\t\t\t}}\n\
\t\t\tif (prepareStatement != null) {{\n\
\t\t\t\ttry {{\n\
\t\t\t\t\tprepareStatement.close();\n\
\t\t\t\t}} catch (SQLException e) {{\n\
\t\t\t\t\tlogger.warn(e.getMessage(), e);\n\
\t\t\t\t}}\n\
\t\t\t}}\n\
\t\t}}\n'
	
def convert(name):
    s1 = re.sub('(.)([A-Z][a-z]+)', r'\1_\2', name)
    return re.sub('([a-z0-9])([A-Z])', r'\1_\2', s1).upper()

def create_path():
	if not os.path.exists(model_path):
		os.makedirs(model_path)
	if not os.path.exists(dao_interface_path):
		os.makedirs(dao_interface_path)
	if not os.path.exists(dao_impl_path):
		os.makedirs(dao_impl_path)
	if not os.path.exists(service_interface_path):
		os.makedirs(service_interface_path)
	if not os.path.exists(service_impl_path):
		os.makedirs(service_impl_path)
create_path()

def create_file(temp):
	model_name = temp.name
	create_path()

	dto_file_path = model_path + os.sep + model_name + model_file_tag
	print(dto_file_path)
	with open(dto_file_path, "a+") as f:
		f.write(dto_content(temp))
		f.close()

	dao_interface_file_path = dao_interface_path + os.sep + model_name + dao_interface_file_tag
	print(dao_interface_file_path)
	with open(dao_interface_file_path, "a+") as f:
		f.write(dao_interface_content(temp))
		f.close()

	dao_impl_file_path = dao_impl_path + os.sep + model_name + dao_impl_file_tag
	with open(dao_impl_file_path, "a+") as f:
		f.write(dao_impl_content(temp))
		f.close()

	service_interface_file_path = service_interface_path + os.sep + model_name + service_interface_file_tag
	with open(service_interface_file_path, "a+") as f:
		f.write(service_interface_content(temp))
		f.close()

	service_impl_file_path = service_impl_path + os.sep + model_name + service_impl_file_tag
	with open(service_impl_file_path, "a+") as f:
		f.write(service_impl_content(temp))
		f.close()

def dto_content(temp):
	model_name = temp.name
	result = ''
	result += 'package {0};\n'.format(model_packge)
	result += 'public class {0}Dto {{\n'.format(model_name.capitalize())
	for attribute in temp.b:
		name = attribute.split(':')[0]
		attr_type = attribute.split(':')[1]
		result += '\tprivate {0} {1};\n'.format(attr_type, name)
	result += write_setter_getter(temp)
	result += dto_constructor(temp)
	result += '}\n'
	return result

def dto_constructor(temp):
	result = ''
	args = ''
	for attribute in temp.b:
		name = attribute.split(':')[0]
		attr_type = attribute.split(':')[1]
		args += '{0} {1}, '.format(attr_type, name)
	args = args[:-2]
	result = "public {0}Dto({1}) {{\n".format(temp.name, args)
	for attribute in temp.b:
		name = attribute.split(':')[0]
		result += "\t\tthis.{0} = {0};\n".format(name)
	result += "}\n\n"
	result += "public {0}Dto() {{}}".format(temp.name)
	return result

def write_setter_getter(temp):
	result = ''
	for attribute in temp.b:
		name = attribute.split(':')[0]
		attr_type = attribute.split(':')[1]

		result += '\tpublic {0} get{1}() {{\n'.format(attr_type, name.capitalize())
		result += '\t\treturn {0};\n'.format(name)
		result += '\t}\n'

		result += '\tpublic void set{0}({1} {2}) {{\n'.format(name.capitalize(), attr_type, name)
		result += '\t\tthis.{0} = {0};\n'.format(name)
		result += '\t}\n'
	return result
	
def dao_interface_content(temp):
	result = ''
	model_name = temp.name
	dao_interface_name = model_name + 'Dao'
	print(dao_interface_name)
	result += 'package {0};\n'.format(dao_interface_package)
	result += 'public interface {0} {{\n'.format(dao_interface_name)
	result += '}\n'
	return result
	
def dao_impl_content(temp):
	result = ''
	model_name = temp.name
	dao_interface_name = model_name + 'Dao'
	dao_impl_name = model_name + 'DaoImpl'
	result += 'package {0};\n\n'.format(dao_impl_package)
	result += 'import {0};\n\n'.format(dao_interface_package + '.' + dao_interface_name)
	result += 'public class {0} implements {1} {{\n'.format(dao_impl_name, dao_interface_name)
	result += '\tprivate final Logger logger = LoggerFactory.getLogger(this.getClass());\n'
	result += '}\n'
	return result

def service_interface_content(temp):
	result = ''
	model_name = temp.name
	service_interface_name = model_name + 'Service'
	result += 'package {0};\n'.format(service_interface_package)
	result += 'public interface {0} {{\n'.format(service_interface_name)
	result += '}\n'
	return result

def service_impl_content(temp):
	result = ''
	model_name = temp.name
	service_interface_name = model_name + 'Service'
	service_impl_name = model_name + 'ServiceImpl'
	result += 'package {0};\n\n'.format(service_impl_package)
	result += 'import {0};\n\n'.format(service_interface_package + '.' + service_interface_name)
	result += 'public class {0} implements {1}, Serializable {{\n'.format(service_impl_name, service_interface_name)
	result += 'private static final long serialVersionUID = 4822474486634242542L;\n'
	result += 'private Transaction transaction;\n'
	result += '}\n'
	return result

def f(x):
	return {
		'create': create_file,
		'edit': edit_file,		
		'delete': delete_file,
		'validator': create_validator
	}[x](temp);

def delete_file(temp):
	print("HEY YO I'M DELETEING THESE FILES!")

def create_validator(temp):
	class_name = temp.name
	validator_dir = project_folder + os.sep + 'rd' + os.sep + 'impl' + os.sep + 'validator'
	validator_pkg = 'rd.impl.validator'
	file_name = validator_dir + os.sep + class_name + "Validator.java"

	content = 'package rd.impl.validator;\n\
\n\
import javax.faces.component.UIComponent;\n\
import javax.faces.context.FacesContext;\n\
import javax.faces.validator.Validator;\n\
import javax.faces.validator.ValidatorException;\n\
\n\
public class {0} implements Validator {{\n\
\n\
\t@Override\n\
\t\tpublic void validate(FacesContext context, UIComponent component,\n\
\t\t\tObject value) throws ValidatorException {{\n\
\t}}\n\
}}\n'
	print(file_name)
	with open(file_name, "a+") as f:
		f.write(content.format(class_name + "Validator"))
		f.close()
	
"""
edit commands:
edit -f Leave -addm getController()
edit -f Leave -adda a:String b:int 
"""
def edit_file(temp):
	file_name = temp.name
	if (temp.attr):
		edit_model_file(temp)
	if (temp.method):
		edit_service_files(temp)

def edit_model_file(temp):
	file_name = model_path + os.sep + temp.name + model_file_tag
	edit_last_line(file_name, new_attribute_content(temp))

def edit_service_files(temp):
	edit_dao_interface(temp)
	edit_dao_impl(temp)
	edit_service_interface(temp)
	edit_service_impl(temp)

def edit_dao_interface(temp):
	file_name = dao_interface_path + os.sep + temp.name + dao_interface_file_tag
	edit_last_line(file_name, dao_int_new_method(temp))

def dao_int_new_method(temp):
	result = ""
	for element in temp.method:
		return_type = element.split(':')[1]
		method_sign = element.split(':')[0]
		p = re.compile("\(.*?\)")
		args = p.search(method_sign).group(0)
		method_name = method_sign.replace(args, "")
		result += '\t'
		if (len(args) == 2):	# no args
			result += "public {0} {1}(Transaction transaction) throws IOException;\n".format(return_type, method_name)
		else:
			result += "public {0} {1}(Transaction transaction, {2}) throws IOException;\n".format(return_type, method_name, args[1:-1])
	return result

def edit_dao_impl(temp):
	file_name = dao_impl_path + os.sep + temp.name + dao_impl_file_tag
	edit_last_line(file_name, dao_impl_new_method(temp))

def dao_impl_new_method(temp):
	result = ""
	for element in temp.method:
		return_type = element.split(':')[1]
		method_sign = element.split(':')[0]
		p = re.compile("\(.*?\)")
		args = p.search(method_sign).group(0)
		method_name = method_sign.replace(args, "")
		result += '\t'
		if (len(args) == 2):	# no args
			result += "public {0} {1}(Transaction transaction) throws IOException {{\n".format(return_type, method_name)
		else:
			result += "public {0} {1}(Transaction transaction, {2}) throws IOException {{\n".format(return_type, method_name, args[1:-1])
		result += dao_impl_method.format(convert(method_name), '' if return_type=='void' else 'return result;')
		result += "\t}\n"
		result += '\tprivate static String {0} = "";\n'.format(convert(method_name))
	return result

def edit_service_interface(temp):
	file_name = service_interface_path + os.sep + temp.name + service_interface_file_tag
	edit_last_line(file_name, serv_int_new_method(temp))

def serv_int_new_method(temp):
	result = ""
	for element in temp.method:
		return_type = element.split(':')[1]
		method_sign = element.split(':')[0]
		result += "\tpublic {0} {1} throws IOException;\n".format(return_type, method_sign)

	return result

def edit_service_impl(temp):
	file_name = service_impl_path + os.sep + temp.name + service_impl_file_tag
	edit_last_line(file_name, serv_impl_new_method(temp))

def serv_impl_new_method(temp):
	result = ""
	for element in temp.method:
		class_name = temp.name
		return_type = element.split(':')[1]
		method_sign = element.split(':')[0]
		p = re.compile("\(.*?\)")
		args = p.search(method_sign).group(0)
		method_name = method_sign.replace(args, "")
		result += "\tpublic {0} {1} throws IOException {{\n".format(return_type, method_sign)

		arg_element = []
		try:
			arg_element = [t.strip() for t in args[1:-1].split(',')].remove('')
		except ValueError:
			pass
		arg_name = ['transaction'] + ([t.split(' ')[1] for t in arg_element] if len(arg_element) > 0 else [])
		return_stmt = ''
		return_obj = ''
		if return_type != 'void':
			return_stmt = 'return result;\n'
			return_obj = return_type + ' result ='
		result += ser_impl_method.format(class_name[0].lower() + class_name[1:] + 'Dao', method_name, return_obj, ', '.join(arg_name), return_stmt) 
		result += "\t}\n"
	return result

def edit_last_line(file, content):
	readFile = open(file)
	lines = readFile.readlines()
	readFile.close()
	w = open(file, 'w')
	lines = [item for item in lines[:-1]]	
	lines.append(content)
	lines.append("}\n")
	w.writelines(lines)	
	w.close()
	
def new_attribute_content(temp):
	result = ""
	for attributepair in temp.attr:
		name = attributepair.split(':')[0]
		attr_type = attributepair.split(':')[1]
		result += "\tprivate {1} {0};\n".format(name, attr_type)
		result += "\tpublic {0} get{1}() {{\n".format(attr_type, name.capitalize());
		result += "\t\treturn this.{0};\n".format(name)
		result += "\t}\n"
		result += "\tpublic void set{0}({1} {2}) {{\n".format(name.capitalize(), attr_type, name)
		result += "\t\tthis.{0} = {0};\n".format(name)
		result += "\t}\n"
	return result

while (command != 'exit'):
	command = prompt.query('>>>> Command: ')
	print('> ', command)
	argTokens = command.split(' ')[1:]
	operator = command.split(' ')[0].lower()
	temp = parser.parse_args(argTokens)
	if (temp.method):
		temp.method = [x.replace('_', ' ') for x in temp.method]
	print(temp)
	# create_file(temp)
	f(operator)
	
# reference links:
# http://stackoverflow.com/questions/15753701/argparse-option-for-passing-a-list-as-option
# http://pymotw.com/2/argparse/
# https://docs.python.org/3.4/library/argparse.html

