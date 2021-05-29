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
        oldSurveys:[["2021-10-05", "ShampooGarden"], ["2021-05-13", "SpecialSoap"]],
        toDelete:[],
        usersWhoSubmitted: ["Giuseppe", "Pino"],
        usersWhoDeleted: ["Marco", "Annulla", "RandomUser"],
        singleUserAnswers: [["Did you like this product?", "yes a lot"], ["Do you find your hair softer?", "no"]], //questions + answers
        userToInspectAge: "",
        userToInspectGender: "",
        userToInspectExpLvl: "ciao",
        surveyToInspect: "",
        userToInspect: "",
        message: "",
        wrongDateChoice: false,
        welcome: false,
        surveyDateChoice: false,
        surveyNewProduct: false,
        surveyQuestions: true,
        surveyCreation: true,
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
            this.surveyDeletion = true;
            this.welcome = false;
        },
        surveysInspectionPage: function() {
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
                this.message = "";
            }
            else
                this.message = "Question already entered.";
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
                    this.message = "";
                } else
                    this.message = "Option already entered.";
            }
            else
                this.message = "Option can't be empty.";
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
                    toSend.set(this.questions[i].question, this.options[i]);
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
            let toSend;
            toSend = Object.fromEntries(this.toDelete);
            axios.post("./AdminSurvey",toSend)
                .then(response => {
                    this.surveyDeletion = false;
                    this.welcome = true;
                    console.log(response.data)
                }).catch(response => {
                    console.log(response.data)
            });
            //to leave only in response=>
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
            axios.post("./AdminSurvey?date=" + this.newSurveyDate).then(response => {
                if (response.data === true) {
                    this.surveyDateChoice = false;
                    this.surveyNewProduct = true;
                    this.message = "";
                }
                else {
                    this.wrongDateChoice = true;
                    this.message = "A survey for this date has already been created. Choose another date.";
                }
            }).catch(response => {
                console.log(response.data)
            });
        },
        sendNewProduct: function (){
            let formData = new FormData();
            formData.append("name", this.newProductName);
            formData.append("image", this.newProductImage);
            axios.post('./AdminSurvey', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            }).then(response => {
                this.surveyNewProduct = false;
                this.surveyQuestions = true;
                console.log(response.data)
            }).catch(response => {
                console.log(response.data)
            });
        },
        getOldSurveys: function(){
            axios.get("/AdminSurvey")
                .then(response => {
                    this.oldSurveys= [];
                    const oldSurveys = response.data;
                    oldSurveys.forEach((survey)=> {
                        let tmp = [];
                        tmp.push(survey.getKey()); //date
                        tmp.push(survey.get(survey.getKey())); //name
                        this.oldSurveys.push(tmp);
                    })
                })
        },
        handleImageUpdate: function(event) {
            this.newProductImage = event.target.files[0];
        },
        resetNewProduct: function(){
            this.surveyDateChoice = true;
            this.surveyNewProduct = false;
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
        },
        submitSurveyToInspect: function(){
            axios.get("./AdminSurvey?date=" + this.surveyToInspect)
                .then(response => {
                    let usersListsObject = response.data;
                    this.usersWhoSubmitted = usersListsObject.submitters;
                    this.usersWhoDeleted = usersListsObject.cancellers;
                    console.log(response.data)
                }).catch(response => {
                console.log(response.data)
            });
        },
        submitUserToInspect: function(){
            axios.post("./AdminSurvey?user=" + this.userToInspect)
                .then(response => {
                    let surveyData = response.data;
                    surveyData.questionAnswersMap.forEach((qAnda)=> {
                        let tmp = [];
                        tmp.push(qAnda.getKey()); //question
                        tmp.push(qAnda.get(qAnda.getKey())); //answer
                        this.singleUserAnswers.push(tmp);
                    })
                    this.userToInspectAge.push(surveyData.age);
                    this.userToInspectGender.push(surveyData.gender);
                    this.userToInspectExpLvl.push(surveyData.expLvl);
                    console.log(response.data)
                }).catch(response => {
                console.log(response.data)
            });
        },
        showSubmissions: function() {
            this.singleSurveyInspection = false;
            this.submissionsInspection = true;
        },
        showCancellations: function() {
            this.singleSurveyInspection = false;
            this.deletionsInspection = true
        },
        showOldSurveysInspection: function() {
            this.singleSurveyInspection = false;
            this.surveysInspection = true;
            this.oldSurveysBool = true;
        },
        showSingleSurvey: function() {
            this.submissionsInspection = false;
            this.singleSurveyInspection = true;
        }
    },
});