//steamtofu-team
//mejaku-project
//cloud function to send email with firestore trigger
const functions = require("firebase-functions");
const admin = require("firebase-admin") 
const nodemailer = require('nodemailer');
admin.initializeApp()

//register the gmail sender account
let transporter = nodemailer.createTransport({
    host: '**************',
    port: '***',
    secure: true,
    auth: {
        user: '**********************', 
        pass: '**********'
    }
});

//this function for send email to student
exports.sendStudentEmail = functions.region('asia-southeast2').firestore
    
    .document('students/{studentsId}')
    .onCreate((snap, context) => {
    const assignment1 = snap.data().assignment1;
    const assignment2 = snap.data().assignment2;
    const assignment3 = snap.data().assignment3;
    const quiz = snap.data().quiz;
    const prediction = snap.data().scoresPrediction;    
    const name = snap.data().name;    
    const student = snap.data().studentMail;
    let conclusion = '';
    let advice = '';

    if (prediction>=75) {
        conclusion = 'PASS';
        advice = 'Your Performance is good. Please keep it up! '
    }else{
        conclusion = 'NOT PASS';
        advice = 'Your Performance is under the Minimum Criteria of Mastery Learning. Please keep it up!';
    };

        const mailOptions = {
            from: `Mejaku Team <**********************>`,
            to: student,
            subject: 'Mejaku - Your Learning Result',
            html:   `<p>Hi, ${name}!</p>
                    <p> Here is your learning result</p>
                    <ul>
                        <li>Quiz01 : ${quiz}</li>
                        <li>Assignment01 : ${assignment1}</li>
                        <li>Assignment02 : ${assignment2}</li>
                        <li>Assignment03 : ${assignment3}</li>
                        <li><b>Predicted Final Score : ${prediction}</b></li>
                        <li><b>Conclusion : ${conclusion}</b></li>
                    </ul><br>
                    <p>${advice}</p><br>
                    <p><i>DISCLAIMER</i></p>
                    <p><i>The real final score may be higher or lower than predicted</i></p>`
        };
        return transporter.sendMail(mailOptions, (error, data) => {
            if (error) {
                console.log(error)
                return
            }
            console.log("Student email successfully sent!")
        });
    });

//this function for send email to parent
exports.sendParentEmail = functions.region('asia-southeast2').firestore
    .document('students/{studentsId}')
    .onCreate((snap, context) => {
    const assignment1 = snap.data().assignment1;
    const assignment2 = snap.data().assignment2;
    const assignment3 = snap.data().assignment3;
    const quiz = snap.data().quiz;
    const prediction = snap.data().scoresPrediction;    
    const name = snap.data().name;    
    const parent = snap.data().parentMail;
    let conclusion = '';
    let advice = 'Please monitor your child\'s self learning session to keep it up.';

    if (prediction>=75) {
        conclusion = 'PASS';
    }else{
        conclusion = 'NOT PASS';
    };

        const mailOptions = {
            from: `Mejaku Team <**********************>`,
            to: parent,
            subject: 'Mejaku - Your Child\'s Learning Result',
            html:   `<p>Hi, ${name} \parents</p>
                    <p> Here is your child's learning result</p>
                    <ul>
                        <li>Quiz01 : ${quiz}</li>
                        <li>Assignment01 : ${assignment1}</li>
                        <li>Assignment02 : ${assignment2}</li>
                        <li>Assignment03 : ${assignment3}</li>
                        <li><b>Predicted Final Score : ${prediction}</b></li>
                        <li><b>Conclusion : ${conclusion}</b></li>
                    </ul>
                    <p>${advice}</p>
                    <p><i>DISCLAIMER</i></p>
                    <p><i>The real final score may be higher or lower than predicted</i></p>`
        };
        return transporter.sendMail(mailOptions, (error, data) => {
            if (error) {
                console.log(error)
                return
            }
            console.log("Parents email successfully sent!")
        });
    });
