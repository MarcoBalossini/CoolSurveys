let index = new Vue ({
    el: "#admin",
    data: {
        activeSurveysDates:[],
        newSurveyDate: "",
        questions: [{ question: "" }],
        options: [[{ option: "" }]],
        multipleChoice: false,
        questionAdded:[],
        oldSurveysDates:["10/05/2021", "13/05/2021"],
        toDelete:[],
        submissions: [["Giuseppe", "yes", "quite bad"], ["Pino", "Bleah"]], //TODO: receive a map of arrays quest (key) -> user + answers (values)
        deletions: ["Marco", "Annulla", "RandomUser"],
        surveyToInspect: "",
        userToInspect: "",
        message: '',
        wrongDateChoice: false,
        welcome: true,
        surveyDateChoice: false,
        surveyCreation: false,
        surveyDeletion: false,
        surveysInspection:false,
        oldSurveys:false,
        singleSurveyInspection:false,
        submissionsInspection: false,
        deletionsInspection:false,
        singleUserInspection:false
    },
    computed: {

        totalSubmissions: function () {
            return [["10/05/2021", this.submissions, this.deletions]]
        }, //elements of the array composed by survey date + its submissions

        submissionOfChosenUser: function() {
            let found = false;
            let i;
            let index;
            for (i = 0; i<this.totalSubmissions.length; i++) {
                //TODO: check date format
               if (this.totalSubmissions[i][0] === this.surveyToInspect) {
                   found = true;
                   index = i;
               }
            }
            if (found) {
                for (let j = 0; j < this.totalSubmissions[index][1].length; j++) {
                    if (this.totalSubmissions[index][1][j][0] === this.userToInspect)
                        return this.totalSubmissions[index][1][j];
                }
            }
            else {
                return "";
            }
        },
        deletionsOfChosenSurvey: function() {
            let i;
            for (i = 0; i<this.totalSubmissions.length; i++) {
                if (this.totalSubmissions[i][0] === this.surveyToInspect)
                    return this.totalSubmissions[i][2];
            }
        }
    },
    methods: {
        addQuestion(questions, index) {
            questions.push({ question: "" });
            this.options.push([{ option: "" }]);
            this.multipleChoice = false;
            this.questionAdded[index] = true;
        },
        setOpenAnswer(questionIndex, options) {
            if (options[questionIndex]!==null && options[questionIndex][0].option !== "")
                options.splice(questionIndex, 1);
            this.multipleChoice = false;
        },
        removeQuestion(index, questions) {
            questions.splice(index, 1);
            this.questionAdded[index] = false;
        },
        addOption(options, optionIndex) {
            if (options[optionIndex].option !== "")
                options.push({ option: "" });
        },
        setMultipleChoice(questionIndex) {
            this.multipleChoice = true;
        },
        removeOption(index, options) {
            options.splice(index, 1);
        },
        resetQuestionsForm(){
            this.questions = [{ question: "" }];
            this.options = [[{ option: "" }]];
            this.multipleChoice = false;
            this.questionAdded= [];
            this.surveyCreation = false;
            this.welcome = true;
        },
        submitQuestionsForm(){
            let toSend = new Map();
            let i;
            if (this.questions.length === this.options.length) {
                //returned a map of questions (keys) -> array of options (value)
                for(i=0; i < this.questions.length; i++) {
                    toSend.set(this.questions[i], this.options[i]);
                }
                const object = Object.fromEntries(toSend);
                axios.post("./AdminSurvey", {
                    object
                }).then(response => {
                    this.leaderboard = true;
                    this.section2 = false;
                    console.log(response.data)

                }).catch(response => {
                    console.log(response.data)
                });
            }
        },
        resetDeletion(){
            this.toDelete = [];
            this.surveyDeletion = false;
            this.welcome = true;
        },
        submitDeletion(){
            axios.post("./AdminSurvey", {
                //send array of dates toDelete
            }).then(response => {

            }).catch(response => {
                console.log(response.data)
            });
            this.surveyDeletion = false;
            this.welcome = true;
        },
        resetNewSurveyDateChoice(){
            this.welcome = true;
            this.surveyDateChoice = false;
            this.newSurveyDate = "";
        },
        checkValidDate(){
            axios.get("", {
                //TODO: get the active surveys dates and check if !== from new survey date
            })
            if (true) {
                this.surveyDateChoice = false;
                this.surveyCreation = true;
            }
            else {
                this.wrongDateChoice = true;
            }
        },
        setSurveyChoice(event) {
            this.surveyToInspect = event.target.innerText;
            this.oldSurveys = false;
            this.singleSurveyInspection = true;
        },
        setUserChoice(event) {
            this.userToInspect = event.target.innerText;
            this.submissionsInspection = false;
            this.singleUserInspection = true;
        }
    },
});