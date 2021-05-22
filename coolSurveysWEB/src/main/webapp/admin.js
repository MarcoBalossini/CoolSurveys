let index = new Vue ({
    el: "#admin",
    data: {
        activeSurveysDates:[],
        newSurveyDate: "",
        questions: [{ question: "" }],
        options: [[{ option: "" }]],
        multipleChoice: false,
        newProductName:"",
        newProductImage:"",
        questionAdded:[],
        oldSurveys:[["10/05/2021", "ShampooGarden"], ["13/05/2021", "SpecialSoap"]],
        toDelete:[],
        usersWhoSubmitted: ["Giuseppe", "Pino"],
        usersWhoDeleted: ["Marco", "Annulla", "RandomUser"],
        singleUserAnswers: [["Did you like this product?", "yes a lot"], ["Do you find your hair softer?", "no"]], //questions + answers
        surveyToInspect: "",
        userToInspect: "",
        message: '',
        wrongDateChoice: false,
        welcome: true,
        surveyDateChoice: false,
        surveyNewProduct: false,
        surveyQuestions: false,
        surveyCreation: false,
        surveyDeletion: false,
        surveysInspection:false,
        oldSurveysBool:false,
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
        createNewSurveyPage: function() {
            this.surveyDateChoice = true;
            this.surveyCreation = true;
            this.welcome = false;
        },
        deleteSurveyPage: function() {
            this.surveyDeletion = true;
            this.welcome = false;
        },
        surveysInspectionPage: function() {
            this.surveysInspection = true;
            this.oldSurveysBool = true;
            this.welcome = false;
        },
        addQuestion: function(questions, index) {
            questions.push({ question: "" });
            this.options.push([{ option: "" }]);
            this.multipleChoice = false;
            this.questionAdded[index] = true;
        },
        setOpenAnswer: function(questionIndex, options) {
            if (options[questionIndex]!==null && options[questionIndex][0].option !== "")
                options.splice(questionIndex, 1);
            this.multipleChoice = false;
        },
        removeQuestion: function(index, questions) {
            questions.splice(index, 1);
            this.questionAdded[index] = false;
        },
        addOption: function(options, optionIndex) {
            if (options[optionIndex].option !== "")
                options.push({ option: "" });
        },
        setMultipleChoice: function(questionIndex) {
            this.multipleChoice = true;
        },
        removeOption: function(index, options) {
            options.splice(index, 1);
        },
        resetQuestionsForm: function(){
            this.questions = [{ question: "" }];
            this.options = [[{ option: "" }]];
            this.multipleChoice = false;
            this.questionAdded= [];
            this.newProductImage="";
            this.newProductName="";
            this.surveyQuestions = false;
            this.surveyNewProduct = true;
        },
        submitQuestionsForm: function(){
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
                    this.welcome = true;
                    this.surveyQuestions = false;
                    this.surveyCreation = false;
                    console.log(response.data)

                }).catch(response => {
                    console.log(response.data)
                });
            }
        },
        resetDeletion: function(){
            this.toDelete = [];
            this.surveyDeletion = false;
            this.welcome = true;
        },
        submitDeletion: function(){
            axios.post("./AdminSurvey", {
                //send array of dates toDelete
            }).then(response => {

            }).catch(response => {
                console.log(response.data)
            });
            this.surveyDeletion = false;
            this.welcome = true;
        },
        resetNewSurveyDateChoice: function(){
            this.welcome = true;
            this.surveyCreation = false;
            this.surveyDateChoice = false;
            this.newSurveyDate = "";
            this.newProductName = "";
        },
        checkValidDate: function(){
            axios.get("", {
                //TODO: get the active surveys dates and check if !== from new survey date
            })
            if (true) {
                this.surveyDateChoice = false;
                this.surveyNewProduct = true;
            }
            else {
                this.wrongDateChoice = true;
            }
        },
        handleImageUpdate: function(event) {
            this.newProductImage = event.target.files[0];
        },
        resetNewProduct: function(){
            this.surveyDateChoice = true;
            this.surveyNewProduct = false;
        },
        submitNewProduct: function(){
            this.surveyNewProduct = false;
            this.surveyQuestions = true;
        },
        setSurveyChoice: function(event) {
            this.surveyToInspect = event.target.innerText;
            this.oldSurveysBool = false;
            this.singleSurveyInspection = true;
        },
        setUserChoice: function(event) {
            this.userToInspect = event.target.innerText;
            this.submissionsInspection = false;
            this.singleUserInspection = true;
        }
    },
});