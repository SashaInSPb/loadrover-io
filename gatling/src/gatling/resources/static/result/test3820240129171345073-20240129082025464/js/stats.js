var stats = {
    type: "GROUP",
name: "All Requests",
path: "",
pathFormatted: "group_missing-name--1146707516",
stats: {
    "name": "All Requests",
    "numberOfRequests": {
        "total": "4",
        "ok": "2",
        "ko": "2"
    },
    "minResponseTime": {
        "total": "23",
        "ok": "210",
        "ko": "23"
    },
    "maxResponseTime": {
        "total": "277",
        "ok": "277",
        "ko": "55"
    },
    "meanResponseTime": {
        "total": "141",
        "ok": "244",
        "ko": "39"
    },
    "standardDeviation": {
        "total": "106",
        "ok": "34",
        "ko": "16"
    },
    "percentiles1": {
        "total": "133",
        "ok": "244",
        "ko": "39"
    },
    "percentiles2": {
        "total": "227",
        "ok": "260",
        "ko": "47"
    },
    "percentiles3": {
        "total": "267",
        "ok": "274",
        "ko": "53"
    },
    "percentiles4": {
        "total": "275",
        "ok": "276",
        "ko": "55"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 2,
    "percentage": 50
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 2,
    "percentage": 50
},
    "meanNumberOfRequestsPerSecond": {
        "total": "0.8",
        "ok": "0.4",
        "ko": "0.4"
    }
},
contents: {
"req_request-post-1302173776": {
        type: "REQUEST",
        name: "request_POST",
path: "request_POST",
pathFormatted: "req_request-post-1302173776",
stats: {
    "name": "request_POST",
    "numberOfRequests": {
        "total": "2",
        "ok": "2",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "210",
        "ok": "210",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "277",
        "ok": "277",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "244",
        "ok": "244",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "34",
        "ok": "34",
        "ko": "-"
    },
    "percentiles1": {
        "total": "244",
        "ok": "244",
        "ko": "-"
    },
    "percentiles2": {
        "total": "260",
        "ok": "260",
        "ko": "-"
    },
    "percentiles3": {
        "total": "274",
        "ok": "274",
        "ko": "-"
    },
    "percentiles4": {
        "total": "276",
        "ok": "276",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 2,
    "percentage": 100
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "0.4",
        "ok": "0.4",
        "ko": "-"
    }
}
    },"req_request-get-1150375302": {
        type: "REQUEST",
        name: "request_GET",
path: "request_GET",
pathFormatted: "req_request-get-1150375302",
stats: {
    "name": "request_GET",
    "numberOfRequests": {
        "total": "2",
        "ok": "0",
        "ko": "2"
    },
    "minResponseTime": {
        "total": "23",
        "ok": "-",
        "ko": "23"
    },
    "maxResponseTime": {
        "total": "55",
        "ok": "-",
        "ko": "55"
    },
    "meanResponseTime": {
        "total": "39",
        "ok": "-",
        "ko": "39"
    },
    "standardDeviation": {
        "total": "16",
        "ok": "-",
        "ko": "16"
    },
    "percentiles1": {
        "total": "39",
        "ok": "-",
        "ko": "39"
    },
    "percentiles2": {
        "total": "47",
        "ok": "-",
        "ko": "47"
    },
    "percentiles3": {
        "total": "53",
        "ok": "-",
        "ko": "53"
    },
    "percentiles4": {
        "total": "55",
        "ok": "-",
        "ko": "55"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 0,
    "percentage": 0
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 2,
    "percentage": 100
},
    "meanNumberOfRequestsPerSecond": {
        "total": "0.4",
        "ok": "-",
        "ko": "0.4"
    }
}
    }
}

}

function fillStats(stat){
    $("#numberOfRequests").append(stat.numberOfRequests.total);
    $("#numberOfRequestsOK").append(stat.numberOfRequests.ok);
    $("#numberOfRequestsKO").append(stat.numberOfRequests.ko);

    $("#minResponseTime").append(stat.minResponseTime.total);
    $("#minResponseTimeOK").append(stat.minResponseTime.ok);
    $("#minResponseTimeKO").append(stat.minResponseTime.ko);

    $("#maxResponseTime").append(stat.maxResponseTime.total);
    $("#maxResponseTimeOK").append(stat.maxResponseTime.ok);
    $("#maxResponseTimeKO").append(stat.maxResponseTime.ko);

    $("#meanResponseTime").append(stat.meanResponseTime.total);
    $("#meanResponseTimeOK").append(stat.meanResponseTime.ok);
    $("#meanResponseTimeKO").append(stat.meanResponseTime.ko);

    $("#standardDeviation").append(stat.standardDeviation.total);
    $("#standardDeviationOK").append(stat.standardDeviation.ok);
    $("#standardDeviationKO").append(stat.standardDeviation.ko);

    $("#percentiles1").append(stat.percentiles1.total);
    $("#percentiles1OK").append(stat.percentiles1.ok);
    $("#percentiles1KO").append(stat.percentiles1.ko);

    $("#percentiles2").append(stat.percentiles2.total);
    $("#percentiles2OK").append(stat.percentiles2.ok);
    $("#percentiles2KO").append(stat.percentiles2.ko);

    $("#percentiles3").append(stat.percentiles3.total);
    $("#percentiles3OK").append(stat.percentiles3.ok);
    $("#percentiles3KO").append(stat.percentiles3.ko);

    $("#percentiles4").append(stat.percentiles4.total);
    $("#percentiles4OK").append(stat.percentiles4.ok);
    $("#percentiles4KO").append(stat.percentiles4.ko);

    $("#meanNumberOfRequestsPerSecond").append(stat.meanNumberOfRequestsPerSecond.total);
    $("#meanNumberOfRequestsPerSecondOK").append(stat.meanNumberOfRequestsPerSecond.ok);
    $("#meanNumberOfRequestsPerSecondKO").append(stat.meanNumberOfRequestsPerSecond.ko);
}
