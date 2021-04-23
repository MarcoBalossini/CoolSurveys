let index = new Vue ({
    el : "#homeApp",
    data: {
        answers: [],
        questions: ['Do you like the daily product?', 'Do you find it comfortable?', 'Do you think it is durable?'],
        options: [['yes', 'no'], null, [0,1]],
        age: 0,
        sex: 0,
        explvl: '',
        scores:[['Michele', 0], ['Gigi', 9] ],
        message: '',
        homepage:true,
        section1:false,
        section2:false,
        leaderboard:false,
        totalCharacters: 0
    },

    //computed properties: dynamic data based on other dynamic data

    computed: {
    },
    methods:{
        charCount: function(index){
            this.totalCharacters = this.answers[index].length;
        },
        resetAll: function() {
            this.answers= [];
            this.questions= [];
            this.options= null;
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
        },
        resetSection2Form: function () {
            this.age='';
            this.sex='';
            this.explvl='';
            this.homepage = true;
            this.section2 = false;
        },
        advanceSurveySection: function() {
            this.section1 = false;
            this.section2 = true;
        },
        submitSurvey: function() {
            let i;
            let questionsAnswersMap = new Map();
            for (i=0; i < this.questions.length; i++) {
                questionsAnswersMap.set(this.questions[i], this.answers[i]);
            }

            function mapToObj(inputMap) {
                let obj = {};

                inputMap.forEach(function(value, key){
                    obj[key] = value
                });

                return obj;
            }
            let object = mapToObj(questionsAnswersMap);

            axios.post("./HandleSurvey", {
                questions: object
            }).then(response => {
                this.leaderboard = true;
                this.section2 = false;
                console.log(response.data)
                //To change html file:
                //window.location.href = "/nome.html";
            }).catch(response => {
                console.log(response.data)
            });
            //inviare age, sex, explvl, answers
        }
    }
});