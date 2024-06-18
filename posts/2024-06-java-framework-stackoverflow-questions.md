---
title: Stackoverflow active questions by Java Frameworks
summary: How do Micronaut, Quarkus, and Spring Boot compare by the number of [Stackoverflow](https://stackoverflow.com) questions?
date_published: 2024-06-18T11:45:42+01:00
keywords:micronaut,spring-boot,stackoverflow,quarkus,grails
---

# [%title]

[%summary]

## June 2024

|Framework | Stackoverflow questions |
|:--- |:---- |
|[Grails](https://stackoverflow.com/questions/tagged/grails)| 29877 |
|[Micronaut](https://stackoverflow.com/questions/tagged/micronaut)| 1824 |
|[Quarkus](https://stackoverflow.com/questions/tagged/quarkus)| 4610 |
|[Spring Boot](https://stackoverflow.com/questions/tagged/spring-boot)| 149254 |

<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script type="text/javascript">
google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);
function drawChart() {
  var data = new google.visualization.DataTable();
  data.addColumn('string', 'Java Framework');
  data.addColumn('number', 'Stackoverflow questions');
  data.addRows([
    ['Micronaut', 1824],
	['Quarkus', 4610],
	['Grails', 29877],
	['Spring Boot', 149254]
   ]);
   var options = {'title':'Stackoverflow Questions by Java framework',
   'width':400,
   'height':300};
   var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
   chart.draw(data, options);
}
</script>
<div id="chart_div"></div>

The oldest the framework, the more questions they have. But it is undeniable the Spring Boot dominance. 