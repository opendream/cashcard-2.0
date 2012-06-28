import th.co.opendream.cashcard.Member
import th.co.opendream.cashcard.RunNo

if(!RunNo.count()) {
  def runno = new RunNo(
            key: 'Member',
            description: '',
            currentNo: 0,
            padSize: 4
        )
  runno.save()
}

def members = Member.findAllByMemberNo('')

members.each {
  it.memberNo =  ctx.runNoService.next('Member')
  it.save()
}