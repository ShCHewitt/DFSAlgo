import csv
import xlrd


# Will be used to convert each file we have of type (month.xlsx to files of name month.csv form).
def xlsx_to_csv(filename, month_name):
    excel_file = xlrd.open_workbook(filename)
    sheet_name = excel_file.sheet_by_name('Sheet1')
    string = month_name + ".csv"
    csv_file = open(string, 'w')
    writer = csv.writer(csv_file)

    for i in range(sheet_name.nrows):
        writer.writerow(sheet_name.row_values(i))

    csv_file.close()


#   Need to make file name for the csv.
def set_up_finish():
    month_names = ['october', 'november', 'december', 'january', 'february', 'march', 'april']
    file_names = ['october.xlsx', 'november.xlsx', 'december.xlsx', 'january.xlsx', 'february.xlsx', 'march.xlsx',
                  'april.xlsx']

    for i in range(len(month_names)):
        xlsx_to_csv(file_names[i], month_names[i])

set_up_finish()
