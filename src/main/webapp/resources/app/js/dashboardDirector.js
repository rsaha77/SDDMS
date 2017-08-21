$(document).ready(function() {
    var initPage = function() {
        switchActiveTab('nav-home');
    }
//  var canvas = document.getElementById("canvas");
//  var ctx = canvas.getContext("2d");
//  ctx.fillStyle = "grey";
//  ctx.fillRect(10, 0, 200, 200);
    var revenueChart = new FusionCharts({
        type: 'doughnut2d',
        renderAt: 'chart-container',
        width: '450',
        height: '450',
        dataFormat: 'json',
        dataSource: {
            "chart": {
                "caption": "<br> <br> <br> <br> Split of Revenue by Product Categories",
                "subCaption": "Last year",
                "numberPrefix": "$",
                "paletteColors": "#0075c2,#1aaf5d,#f2c500,#f45b00,#8e0000",
                "bgColor": "#ffffff",
                "showBorder": "0",
                "use3DLighting": "0",
                "showShadow": "0",
                "enableSmartLabels": "0",
                "startingAngle": "310",
                "showLabels": "0",
                "showPercentValues": "1",
                "showLegend": "1",
                "legendShadow": "0",
                "legendBorderAlpha": "0",
                "defaultCenterLabel": "Total revenue: $64.08K",
                "centerLabel": "Revenue from $label: $value",
                "centerLabelBold": "1",
                "showTooltip": "0",
                "decimals": "0",
                "captionFontSize": "14",
                "subcaptionFontSize": "14",
                "subcaptionFontBold": "0"
            },
            "data": [
                {
                    "label": "Fast Food Restaurant",
                    "value": "28504"
                }, 
                {
                    "label": "Health Stores",
                    "value": "14633"
                }, 
                {
                    "label": "Workplace",
                    "value": "10507"
                }, 
                {
                    "label": "Movie Theatres",
                    "value": "4910"
                }
            ]
        }
    }).render();
    initPage();
});