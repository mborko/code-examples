import smtplib
import getpass
import datetime

passwd = getpass.getpass("Gmail-Password: ")
user = "USERNAME@gmail.com"

sender = user
recipients = "receiver1@mailinator.com receiver2@mailinator.com".split()
subject = "acknowledgement of receipt"

now = datetime.datetime.now()

msg = ("From: %s\r\nTo: %s\r\nSubject: %s\r\n"
        % (sender, ", ".join(recipients), subject))

msg = msg + "Testmail generated @" + str(now)

def sendmail():
    try:
        server = smtplib.SMTP_SSL('smtp.gmail.com', 465)
        server.set_debuglevel(0)
        server.ehlo()
        server.login(user, passwd)
        server.sendmail(sender, recipients, msg)
        server.quit()
        print("Email sent successfully!")
    except smtplib.SMTPAuthenticationError as e:
        print("Username and Password not accepted.")
        return False
    except:
        print("Unable to send the email!")
        return False
    return True

while sendmail() != True:
    try:
        passwd = getpass.getpass("Gmail-Password: ")
    except KeyboardInterrupt as e:
        print("\nUser interrupt detected ...")
        print("Email was not sent. Please check your connectivity!")
        break

