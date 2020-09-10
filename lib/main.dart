import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: StepCounter(title: 'StepCounter Demo Home Page'),
    );
  }
}

void startServiceInPlatform () async
{
  if (Platform.isAndroid) {
    var methodChannel = MethodChannel("mk.learner.pedometer/stepcounter");
     String data = await methodChannel.invokeMethod("startService");
     debugPrint(data);
  }
}

class StepCounter extends StatefulWidget {
  final String title;

  const StepCounter({Key key, this.title}) : super(key: key);
  @override
  _StepCounterState createState() => _StepCounterState();
}

class _StepCounterState extends State<StepCounter> {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: widget.title,
      home:  Scaffold(
      appBar: AppBar(title: Text( widget.title)),
      body: Center(
        child: Column(
          children: <Widget>[
            SizedBox(height: 80),
            Text("Today Step Count",style: TextStyle(fontSize: 20),),
            SizedBox(height: 40),
            RaisedButton(
              onPressed: () {
                 startServiceInPlatform();
              },
              child: const Text(
                  'Start Service Button',
                  style: TextStyle(fontSize: 20)
              ),
            ),
          ],
        ),
      )
      ),
    );
  }
}
