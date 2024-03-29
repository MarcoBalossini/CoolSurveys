let index = new Vue ({
    el : "#homeApp",
    data: {
        productOfTheDay: "",
        productImageSrc: "",
        productReviews: [],
        answers1: [],
        answers2: [],
        questions1: [],
        questions2: [],
        questions2Type: [],
        options1: [],
        options2: [],
        age: 0,
        sex: 0,
        explvl: '',
        scores:[],
        message: '',
        homepage:true,
        section1:false,
        section2:false,
        leaderboard:false,
        greetings:false,
        totalCharacters: 0
    },

    //computed properties: dynamic data based on other dynamic data

    computed: {

    },
    methods:{
        charCount: function(index){
            this.totalCharacters = this.answers1[index].length;
        },
        resetSurvey: function() {
            this.answers1= [];
            this.answers2= [];
            this.questions1= [];
            this.questions2= [];
            this.options1= [];
            this.options2= [];
            this.questions2Type= [];
            this.message = '';
        },
        resetAll: function() {
            this.resetSurvey();
            this.sex = 0;
            this.age = 0;
            this.explvl = '';
            this.scores = [];
            this.message = '';
            this.homepage = false;
            this.section1 = false;
            this.section2 = false;
            this.leaderboard = false;
            this.totalCharacters = 0;
        },
        resetSection1Form: function () {
            this.answers=[];
            if (this.totalCharacters !==0)
                this.totalCharacters = 0;
            this.homepage = true;
            this.section1 = false;
            this.message = '';
        },
        resetSection2Form: function () {
            this.age='';
            this.sex='';
            this.explvl='';
            this.homepage = true;
            this.section2 = false;
            this.message = '';
        },
        resetAnswers: function () {
            this.answers1= [];
            this.answers2= [];
            this.message = '';
        },

        getImageSrc: function() {
            axios.get("./static/product")
            .then(response => {
                this.productImageSrc = "data:image/png;base64, " + response.data;
            })
            .catch(error => {
                console.log(error.response.data);
                this.message = error.response.data;
            })
        },

        getReviews: function() {
            axios.get("./home/reviews")
            .then(response => {
                this.productReviews = response.data;
            })
            .catch(error => {
                console.log(error.response.data);
                this.message = error.response.data;
            })
        },

        getProductOfTheDayName: function() {
            axios.get("./static/product?param=prodName")
                .then(response => {
                    this.productOfTheDay = response.data;
                })
                .catch(error => {
                    this.productOfTheDay = "";
                    console.log(error.response.data);
                    this.message = error.response.data;
                });
        },

        receiveSurvey: function() {

            this.resetSurvey();
            axios.get("./HandleSurvey")
                .then(response => {
                    const survey = response.data;
                    const questions = survey.questions;
                    this.productOfTheDay = survey.name;
                    questions.forEach((question)=> {
                        if (question.section === 1) {
                            this.questions1.push(question.question);
                            let tmp = [];
                            question.options.forEach((option)=> {
                                tmp.push(option.text);
                            })
                            this.options1.push(tmp);
                        }
                        else if (question.section === 2) {
                            this.questions2.push(question.question);

                            this.questions2Type.push(question.type);

                            let tmp = [];
                            question.options.forEach((option)=> {
                                tmp.push(option.text);
                            })
                            this.options2.push(tmp);
                        }
                        else
                            console.log("Section not found for question n." + question.number);
                    });
                    this.homepage = false;
                    this.section1 = true;
                })
                .catch(error => {
                    console.log(error.response.data);
                    this.message = error.response.data;
                })
        },

        advanceSurveySection: function() {
            this.section1 = false;
            this.section2 = true;
        },

        submitSurvey: function() {
            let i;
            let questionsAnswersMap = new Map();
            for (i=0; i < this.questions1.length; i++) {
                questionsAnswersMap.set(this.questions1[i], this.answers1[i]);
            }

            for (i=0; i < this.questions2.length; i++) {
                if(this.answers2[i] === undefined)
                    this.answers2[i] = "";
                questionsAnswersMap.set(this.questions2[i], this.answers2[i]);
            }

            function mapToObj(inputMap) {
                let obj = {};

                inputMap.forEach(function(value, key){
                    obj[key] = value
                });

                return obj;
            }
            let object = mapToObj(questionsAnswersMap);

            axios.post("./HandleSurvey", object)
                .then(response => {
                this.greetings = true;
                this.section2 = false;
                this.message = "";
                console.log(response.data);
                //To change html file:
                //window.location.href = "/nome.html";
            }).catch(error => {
                this.message = error.response.data;
                console.log(error.response.data);
            });
        },

        receiveLeaderboard: function() {
            this.resetAnswers();
            axios.get("./Leaderboard")
                .then(response => {
                    this.scores = [];
                    const leaderboard = response.data;
                    leaderboard.forEach((user)=> {
                        let tmp = [];
                        tmp.push(user.username);
                        tmp.push(user.points);
                        this.scores.push(tmp);
                        })
                    this.leaderboard = true;
                    this.homepage = false;
                    if (this.greetings === true)
                        this.greetings = false;
                })
                .catch(error => {
                    console.log(error.response.data);
                    this.message = error.response.data;
                })
        },

        goToHomepage: function() {
            if (this.leaderboard === true)
                this.leaderboard = false;
            else if (this.section1 === true)
                this.section1 = false;
            else if (this.greetings === true)
                this.greetings = false;
            this.homepage = true;
            //this.message = '';
        },

        goToPrevSection: function () {
            this.section1 = true;
            this.section2 = false;
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
    //Operations to be executed when userHome is loaded
    beforeMount() {
        this.getImageSrc();
        this.getReviews();
        this.getProductOfTheDayName();
    }
});