Attribute VB_Name = "Module"
Option Explicit

' 現在選択しているセル
Public currentSelect As Range

' xml一時読み込み用
Public xml As MSXML2.DOMDocument

Public Sub エビデンス貼り付けv3()
    
    ' 選択位置を起点にスタート
    Set currentSelect = cells(Selection.row, Selection.Column)
    
    ' 修正後フォルダー取得
    Dim afterPath As String
    afterPath = GetFolderPath("修正後")
    If "" = afterPath Then
        MsgBox "未入力でもOKだと言ったな。" & vbLf & "あれは嘘だ。修正後は入れないとだめだ。", vbExclamation, _
        "フォルダ内のファイル名一覧取得"
    End If
    
    ' 修正前フォルダー取得
    Dim beforePath As String
    beforePath = GetFolderPath("修正前")
    
    ' 修正後フォルダー内のエビデンスフォルダー一覧を取得
    Dim afterFolderList As Collection
    Set afterFolderList = New Collection
    Set xml = New MSXML2.DOMDocument
    xml.Load (afterPath & "\" & "FolderList.xml")
    Dim afterFolders As IXMLDOMNode
    Set afterFolders = xml.SelectSingleNode("//folderList")
    Dim afterFolder As IXMLDOMNode
    For Each afterFolder In afterFolders.ChildNodes
        afterFolderList.Add afterFolder.text
    Nexe afterFolder
    
    
    
    
    
    
    
    
    
    
    
    ' 修正後フォルダー内のinfoファイル一覧取得
    Dim afterInfoList As Collection
    Set afterInfoList = GetInfoFiles(afterPath)
    
    ' 修正前フォルダー内のinfoファイル一覧取得
    Dim beforeInfoList As Collection
    Set beforeInfoList = GetInfoFiles(beforePath)
    
    ' infoファイル数からエビデンス数を取得
    Dim infoCount As Long
    infoCount = afterInfoList.Count
    If infoCount < beforeInfoList.Count Then
        infoCount = beforeInfoList.Count
    End If
    
    ' エビデンス数だけ繰り返し
    Dim index As Long
    For index = 0 To infoCount
        ' orderからinfo取得
        Dim infoXml As MSXML2.DOMDocument
        Set infoXml = GetInfoByOrder(afterInfoList, index)
        
        ' node格納用
        Dim node As IXMLDOMNode
        
        ' コンテンツノード取得
        Set node = infoXml.SelectSingleNode("//evidenceInformation//contents")
        ' コンテンツ書き込み
        WriteContents (node.text)
        
        
        
    Next index
    
    
    
    
    
End Sub

' フォルダーパス取得用のダイアログ表示
Private Function GetFolderPath(boa As String) As String
    Dim xlAPP As Application
    Set xlAPP = Application
    GetFolderPath = xlAPP.InputBox(boa & "のエビデンスのフォルダ名を入力して下さい。" & vbLf & "無い場合は未入力でＯＫです。", _
                                 "フォルダ内のファイル名一覧取得", "C:\")
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
    file = Dir(folder & "*.info", vbNormal)
    
    ' infoファイルの一覧を取得
    Dim infoList As New Collection
    Do While strFileName <> ""
        
        ' infoファイルをコレクションに追加
        infoList.Add file
        
        ' 次のinfoファイルへ
        file = Dir()
    Loop
    Set GetInfoFiles = infoList
    
End Function

' infoリストの中から指定したorderのxmlを取得
Private Function GetIndoByOrder(list As Collection, order As Long) As MSXML2.DOMDocument
    
    infoリストのサイズ分繰り返し
    Dim index As Long
    For index = 0 To list.Count
        ' xmlオブジェクト作成
        Set xml = New MSXML2.DOMDocument
        
        ' infoファイル一時格納
        Dim infoFile As String
        infoFile = list.Item(index)
        
        ' infoファイル読み込み
        xml.Load (infoFile)
        
        ' orderの値を確認
        Dim orderNode As IXMLDOMNode
        Set orderNode = xml.SelectSingleNode("//evidenceInformation//order")
        If orderNode.text = order Then
            Set GetIndoByOrder = xml
            Exit Function
        End If
        
        Set xml = Nothing
    Next index
    
    ' 見つからない時はNothingをリターン
    Set GetIndoByOrder = Nothing
    
End Function

' エビデンス№書き出し
Private Function WriteEvidenceNo(text As String)
    currentSelect.Value = text
    Set currentSelect = currentSelect.Offset(0, 1)
End Function

' コンテンツ書き出し
Private Function WriteContents(contents As String)
    Dim lines As Variant
    Dim line As String
    
    ' コンテンツを改行コードで分割
    Set lines = Split(contents, Chr(10))
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


