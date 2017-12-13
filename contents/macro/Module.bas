Attribute VB_Name = "Module"
Option Explicit

' 現在選択しているセル
Dim currentSelect As Range

' ***** Config *****

Const eviNoSeparate As Long = 1
Const abbSeparate As Long = 20

' ******************


' xml一時読み込み用
Dim xml As MSXML2.DOMDocument60

Public Sub エビデンス貼り付けv3()
    
    ' 選択位置を起点にスタート
    Set currentSelect = cells(Selection.row, Selection.Column)
    
    ' エビデンスフォルダー取得
    Dim path As String
    path = GetFolderPath()
    
    ' 修正後フォルダー
    Dim afterPath As String
    afterPath = path & "\after"
    
    ' 修正前フォルダー
    Dim beforePath As String
    beforePath = path & "\before"
    
    ' 修正後フォルダー内のエビデンスフォルダー一覧を取得
    ' 修正前フォルダー内のエビデンスフォルダー一覧は修正後と同じはずだから確認しない
    If Dir(afterPath, vbDirectory) <> "" Then
        Dim afterFolderList As Collection
        Set afterFolderList = New Collection
        Set xml = New MSXML2.DOMDocument60
        xml.Load (afterPath & "\" & "FolderList.xml")
        Dim afterFolders As IXMLDOMNode
        Set afterFolders = xml.SelectSingleNode("//folderList")
        Dim afterFolder As IXMLDOMNode
        For Each afterFolder In afterFolders.ChildNodes
            afterFolderList.Add afterFolder.text
        Next afterFolder
    Else
        MsgBox "修正後フォルダーがありません。", Buttons:=vbCritical
        Exit Sub
    End If
    
    ' フォルダーで繰り返し
    Dim folder As Variant
    For Each folder In afterFolderList
        
        Dim startSelect As Range
        Set startSelect = Range(currentSelect.Address)
        
        ' 修正後フォルダー内のinfoファイル一覧取得
        Dim afterInfoList As Collection
        Set afterInfoList = GetInfoFiles(afterPath & "\" & folder)
        
        ' infoファイル数からエビデンス数を取得
        Dim infoCount As Long
        infoCount = afterInfoList.Count
        
        ' エビデンス数だけ繰り返し
        Dim index As Long
        For index = 1 To infoCount
            
            ' orderからinfo取得
            Dim infoXml As MSXML2.DOMDocument60
            Set infoXml = GetInfoByOrder(afterInfoList, index - 1)
            
            ' エビデンス№取得
            Dim eviNo As String
            eviNo = infoXml.SelectSingleNode("//evidenceInformation//evidence_name").text
            ' エビデンス№書き込み
            WriteEvidenceNo eviNo
            
            ' エビデンス№セル取得
            Dim eviNoCell As Range
            Set eviNoCell = Range(infoXml.SelectSingleNode("//evidenceInformation//evi_no_address").text)
            
            ' テストスウィートシートに移動
            ActiveWorkbook.Worksheets(eviNoCell.Parent.Name).Activate
            
            ' エビデンス№セルに値を入れる
            eviNoCell.Value = eviNoCell.Value & eviNo & vbLf
            
            ' エビデンスシートに戻る
            ActiveWorkbook.Worksheets(currentSelect.Parent.Name).Activate
                        
            ' コンテンツノード取得
            Dim contents As String
            contents = infoXml.SelectSingleNode("//evidenceInformation//contents").text
            ' コンテンツ書き込み
            WriteContents contents
            
            ' エビデンス画像関連の値取得
            Dim imageName As String
            imageName = afterPath & "\" & folder & "\" & infoXml.SelectSingleNode("//evidenceInformation//image_name").text
            Dim imageWidth As Long
            imageWidth = infoXml.SelectSingleNode("//evidenceInformation//image_width").text
            Dim imageHeight As Long
            imageHeight = infoXml.SelectSingleNode("//evidenceInformation//image_height").text
            ' エビデンス画像貼り付け
            WriteEvidenceImage imageName, imageWidth, imageHeight
            
            Set currentSelect = currentSelect.Offset(imageHeight / currentSelect.Height, -eviNoSeparate)
            
        Next index
        
        
        ' 修正前フォルダー内のinfoファイル一覧取得
        Dim beforeInfoList As Collection
        If Dir(beforePath, vbDirectory) <> "" Then
            Set beforeInfoList = GetInfoFiles(beforePath & "\" & folder)
            Dim buf_currentSelect As Range
            Set buf_currentSelect = Range(currentSelect.Address)
            Set currentSelect = startSelect.Offset(0, abbSeparate)
        Else
            GoTo CONTINUE
        End If
        
        ' infoファイル数からエビデンス数を取得
        infoCount = beforeInfoList.Count
        
        ' エビデンス数だけ繰り返し
        For index = 1 To infoCount
            
            ' orderからinfo取得
            Set infoXml = GetInfoByOrder(beforeInfoList, index - 1)
            
            ' コンテンツノード取得
            contents = infoXml.SelectSingleNode("//evidenceInformation//contents").text
            ' コンテンツ書き込み
            WriteContents contents
            
            ' エビデンス画像関連の値取得
            imageName = beforePath & "\" & folder & "\" & infoXml.SelectSingleNode("//evidenceInformation//image_name").text
            imageWidth = infoXml.SelectSingleNode("//evidenceInformation//image_width").text
            imageHeight = infoXml.SelectSingleNode("//evidenceInformation//image_height").text
            ' エビデンス画像貼り付け
            WriteEvidenceImage imageName, imageWidth, imageHeight
            
            Set currentSelect = currentSelect.Offset(imageHeight / currentSelect.Height, 0)
            
        Next index
        
        Set currentSelect = Range(buf_currentSelect.Address)
        
CONTINUE:
    Next folder
    
End Sub

' フォルダーパス取得用のダイアログ表示
Private Function GetFolderPath() As String
    Dim xlAPP As Application
    Set xlAPP = Application
    GetFolderPath = xlAPP.InputBox("エビデンスの格納フォルダ名を入力して下さい。", _
                                 "フォルダ選択", "C:\")
    Set xlAPP = Nothing
End Function

' infoファイルの一覧を取得
Private Function GetInfoFiles(folder As String) As Collection
    
    ' 空文字なら空のコレクションをリターン
    If "" = folder Then
        Set GetInfoFiles = New Collection
        Exit Function
    End If
        
    Dim file As String
    file = Dir(folder & "\*.info", vbNormal)
    
    ' infoファイルの一覧を取得
    Dim infoList As New Collection
    Do While file <> ""
        
        ' infoファイルをコレクションに追加
        infoList.Add folder & "\" & file
        
        ' 次のinfoファイルへ
        file = Dir()
    Loop
    Set GetInfoFiles = infoList
    
End Function

' infoリストの中から指定したorderのxmlを取得
Private Function GetInfoByOrder(list As Collection, order As Long) As MSXML2.DOMDocument60
    
    ' infoリストのサイズ分繰り返し
    Dim index As Long
    For index = 1 To list.Count
        ' xmlオブジェクト作成
        Set xml = New MSXML2.DOMDocument60
        
        ' infoファイル一時格納
        Dim infoFile As String
        infoFile = list.Item(index)
        
        ' infoファイル読み込み
        xml.Load (infoFile)
        
        ' orderの値を確認
        Dim orderNode As IXMLDOMNode
        Set orderNode = xml.SelectSingleNode("//evidenceInformation//order")
        If orderNode.text = order Then
            Set GetInfoByOrder = xml
            Exit Function
        End If
        
        Set xml = Nothing
    Next index
    
    ' 見つからない時はNothingをリターン
    Set GetInfoByOrder = Nothing
    
End Function

' エビデンス№書き出し
Private Function WriteEvidenceNo(text As String)
    currentSelect.Value = text
    Set currentSelect = currentSelect.Offset(0, eviNoSeparate)
End Function

' コンテンツ書き出し
Private Function WriteContents(contents As String)
    Dim lines As Variant
    Dim line As Variant
    
    ' コンテンツを改行コードで分割
    lines = Split(contents, "\n")
    For Each line In lines
        
        ' 文が空文字じゃないなら
        If line <> "" Then
            
            ' セルに値を入れて
            currentSelect.Value = line
            
            ' 次のセルへ
            Set currentSelect = currentSelect.Offset(1, 0)
        End If
    Next line
End Function

' エビデンス画像貼り付け セルの移動分を返却
Private Function WriteEvidenceImage(imagePaht As String, imageWidth As Long, imageHeight As Long)
    currentSelect.Activate
    Dim imageShape As Shape
    Set imageShape = ActiveSheet.Shapes.AddPicture( _
        Filename:=imagePaht, _
        LinkToFile:=False, _
        SaveWithDocument:=True, _
        Left:=Selection.Left, _
        Top:=Selection.Top, _
        Width:=imageWidth, _
        Height:=imageHeight)
    Set imageShape = Nothing
End Function


