import th.co.opendream.cashcard.Member

def members = Member.findAllByMemberNo('')

println members.size()

members.each {
  it.memberNo =  ctx.runNoService.next('Member')
  it.save()
}