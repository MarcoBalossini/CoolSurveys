let index = new Vue ({
    el: "#admin",
    data: {
        activeSurveysDates:[],
        newSurveyDate: "",
        questions: [{ question: "" }],
        options: [[{ option: "" }]],
        newProductName:"",
        newProductImage:"",
        questionAdded:[],
        optionAdded:[],
        multipleChoice: false,
        oldSurveys:[],
        toDelete:[],
        usersWhoSubmitted: [],
        usersWhoDeleted: [],
        singleUserAnswers: [], //questions + answers
        userToInspectAge: "",
        userToInspectGender: "",
        userToInspectExpLvl: "",
        surveyToInspect: "",
        userToInspect: "",
        internal_message: "",
        message: "",
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
    },
    methods: {
        createNewSurveyPage: function() {
            this.surveyDateChoice = true;
            this.surveyCreation = true;
            this.welcome = false;
        },
        deleteSurveyPage: function() {
            this.getOldSurveys();
            this.surveyDeletion = true;
            this.welcome = false;
        },
        surveysInspectionPage: function() {
            this.getOldSurveys();
            this.surveysInspection = true;
            this.oldSurveysBool = true;
            this.welcome = false;
        },
        addQuestion: function(questions, options, index) {
            let found;
            let i = 0;
            questions.forEach((question)=> {
                i++;
                let questionsNum = questions.length;
                let newQuestion = questions[questionsNum-1].question;
                if (questionsNum !== 1 && i !== questionsNum && question.question === newQuestion) {
                    found = true;
                    this.questionAdded[index] = false;
                }
            })
            if (!found) {
                questions.push({ question: "" });
                this.optionAdded = [];
                options.push([{ option: "" }]);
                this.multipleChoice = false;
                this.questionAdded[index] = true;
                this.internal_message = "";
            }
            else
                this.internal_message = "Question already entered.";
        },
        showOptions(questionIndex){
            let maxOptionsReached = true;
            if (this.options[questionIndex]!=null)
                maxOptionsReached = this.options[questionIndex].length > 4;
            return this.multipleChoice && !maxOptionsReached && this.questionAdded[questionIndex] !== true;
        },
        maxOptionsReached(questionIndex){
            let maxOptionsReached = true;
            if (this.options[questionIndex]!=null)
                maxOptionsReached = this.options[questionIndex].length > 4;
            return this.multipleChoice && maxOptionsReached && this.questionAdded[questionIndex] !== true;
        },
        setOpenAnswer: function(questionIndex) {
            this.options[questionIndex]= [{ option: "" }];
            this.optionAdded = [];
            this.multipleChoice = false;
        },
        setMultipleChoice: function() {
            this.multipleChoice = true;
        },
        removeOption: function(index, options) {
            options.splice(index, 1);
            this.optionAdded[options.length-1] = false;
        },
        removeQuestion: function(index, questions, options) {
            questions.splice(index, 1);
            options.splice(index, 1);
            this.questionAdded[index] = false;
        },
        addOption: function(options, optionIndex) {
            let found;
            let i = 0;
            if (options[optionIndex].option !== "") {
                let optionsNum = options.length;
                    options.forEach((option) => {
                        i++;
                        let newOption = options[optionsNum - 1].option;
                        if (optionsNum !== 1 && i !== optionsNum && option.option === newOption) {
                            found = true;
                            this.optionAdded[optionIndex] = false;
                        }
                    })
                if (!found) {
                    options.push({ option: "" });
                    this.optionAdded[optionIndex] = true;
                    this.internal_message = "";
                } else
                    this.internal_message = "Option already entered.";
            }
            else
                this.internal_message = "Option can't be empty.";
        },
        resetQuestionsForm: function(){
            this.message = "";
            this.questions = [{ question: "" }];
            this.options = [[{ option: "" }]];
            this.multipleChoice = false;
            this.questionAdded= [];
            this.newProductImage="";
            this.newProductName="";
            this.surveyQuestions = false;
            this.surveyNewProduct = true;
        },
        redoQuestionsForm: function() {
            this.questions = [{ question: "" }];
            this.options = [[{ option: "" }]];
            this.multipleChoice = false;
        },
        goToNewQuestionsPage() {
            this.surveyNewProduct = false;
            this.surveyQuestions = true;
        },
        checkValidDate: function(){
            axios.post("./AdminSurvey?date=" + this.newSurveyDate).then(response => {
                if (response.data === true) {
                    this.surveyDateChoice = false;
                    this.surveyNewProduct = true;
                    this.internal_message = "";
                }
                else {
                    this.wrongDateChoice = true;
                    this.internal_message = "A survey for this date has already been created. Choose another date.";
                }
            }).catch(error => {
                console.log(error.response.data);
                this.message = error.response.data;
            });
        },
        submitQuestionsForm: function() {
            let toSend = new Map();
            let i;
            if (this.questions.length === this.options.length) {
                //returned a map of questions (keys) -> array of options (value)

                this.questions.pop();
                for(i=0; i < this.questions.length; i++) {
                    this.options[i].pop();
                    toSend.set(this.questions[i].question, this.options[i]);
                }

                let questionsMap = Object.fromEntries(toSend);

                let formData = new FormData();
                formData.append("date", this.newSurveyDate);
                formData.append("name", this.newProductName);
                formData.append("image", this.newProductImage);
                formData.append("questionsMap", JSON.stringify(questionsMap));

                axios.post("./AdminSurvey", formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                }).then(response => {
                    this.welcome = true;
                    this.surveyQuestions = false;
                    this.surveyCreation = false;
                    console.log(response.data)
                }).catch(error => {
                    this.message = error.response.data;
                    console.log(error.response.data);
                    this.redoQuestionsForm();
                });
            }
        },
        resetDeletion: function(){
            this.message = "";
            this.toDelete = [];
            this.surveyDeletion = false;
            this.welcome = true;
        },
        submitDeletion: function(){
            let toSend = [];

            for (const value of this.toDelete) {
                toSend.push(value);
            }

            axios.post("./admin/delete", toSend)
                .then(response => {
                    this.surveyDeletion = false;
                    this.welcome = true;
                    console.log(response.data);
                    this.surveyDeletion = false;
                    this.welcome = true;
                }).catch(error => {
                    console.log(error.response.data);
                    this.message = error.response.data;
            });
        },
        resetNewSurveyDateChoice: function(){
            this.message = "";
            this.welcome = true;
            this.surveyCreation = false;
            this.surveyDateChoice = false;
            this.newSurveyDate = "";
            this.newProductName = "";
        },
        getOldSurveys: function(){
            axios.get("./AdminSurvey")
                .then(response => {
                    this.oldSurveys= [];
                    const oldSurveys = response.data;
                    Object.entries(oldSurveys).forEach(([key, value]) => {
                        let tmp = [];
                        tmp.push(key); //date
                        tmp.push(value); //name
                        this.oldSurveys.push(tmp);
                    });
                    if (this.oldSurveys.length === 0)
                        this.message = "No old survey available";
                    else
                        this.message = '';
                })
                .catch(error => {
                    console.log(error.response.data);
                    this.message = error.response.data;
                })
        },
        handleImageUpdate: function(event) {
            this.newProductImage = event.target.files[0];
        },
        resetNewProduct: function(){
            this.message = "";
            this.surveyDateChoice = true;
            this.surveyNewProduct = false;
        },
        setSurveyChoice: function(event) {
            this.surveyToInspect = event.target.innerText;
            this.oldSurveysBool = false;
            this.singleSurveyInspection = true;
            this.submitSurveyToInspect();
        },
        setUserChoice: function(event) {
            this.userToInspect = event.target.innerText;
            this.submissionsInspection = false;
            this.singleUserInspection = true;
            this.submitUserToInspect();
        },
        submitSurveyToInspect: function(){
            axios.get("./AdminSurvey?date=" + this.surveyToInspect)
                .then(response => {
                    this.usersWhoSubmitted = [];
                    this.usersWhoDeleted = [];
                    let usersListsObject = response.data;
                    this.usersWhoSubmitted = usersListsObject.submitters;
                    this.usersWhoDeleted = usersListsObject.cancellers;
                    console.log(response.data);
                })
                .catch(error => {
                this.message = error.response.data;
                console.log(error.response.data);
            });
        },
        submitUserToInspect: function(){
            axios.get("./AdminSurvey?user=" + this.userToInspect + "&date=" + this.surveyToInspect)
                .then(response => {
                    let surveyData = response.data;
                    this.singleUserAnswers = [];
                    Object.entries(surveyData.questionAnswersMap).forEach(([key, value]) => {
                        let tmp = [];
                        tmp.push(key); //question
                        tmp.push(value); //answer
                        this.singleUserAnswers.push(tmp);
                    });
                    this.userToInspectAge = surveyData.age;
                    this.userToInspectGender = surveyData.gender;
                    this.userToInspectExpLvl = surveyData.expLvl;
                    console.log(response.data);
                })
                .catch(error => {
                console.log(error.response.data);
                this.message = error.response.data;
            });
        },
        showSubmissions: function() {
            this.singleSurveyInspection = false;
            this.submissionsInspection = true;
            if (this.oldSurveys.length === 0)
                this.message = "No submissions yet for that questionnaire.";
            else
                this.message = '';
        },
        showCancellations: function() {
            this.singleSurveyInspection = false;
            this.deletionsInspection = true
            if (this.oldSurveys.length === 0)
                this.message = "No cancellations for that questionnaire.";
            else
                this.message = '';
        },
        showOldSurveysInspection: function() {
            this.singleSurveyInspection = false;
            this.surveysInspection = true;
            this.oldSurveysBool = true;
        },
        showSingleSurvey: function() {
            this.message = '';
            this.submissionsInspection = false;
            this.singleSurveyInspection = true;
        },

        logout: function() {
            axios.get("./logout")
                .then(response=>{
                    window.location.href = "index.html";
                })
                .catch(error=> {
                    console.log(error.response.data);
                    this.message = error.response.data;
                })
        }
    },
});